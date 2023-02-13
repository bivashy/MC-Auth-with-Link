package me.mastercapexd.auth.management;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.account.factories.AccountFactory;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.creators.AuthenticationStepCreator;
import me.mastercapexd.auth.authentication.step.steps.NullAuthenticationStep;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.config.message.context.MessageContext;
import me.mastercapexd.auth.config.message.proxy.ProxyMessageContext;
import me.mastercapexd.auth.proxy.ProxyCore;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.storage.AccountStorage;

public class DefaultLoginManagement implements LoginManagement {
    private final ProxyPlugin plugin;
    private final ProxyCore core;
    private final PluginConfig config;
    private final AccountFactory accountFactory;
    private final AccountStorage accountStorage;

    public DefaultLoginManagement(ProxyPlugin plugin) {
        this.plugin = plugin;
        this.core = plugin.getCore();
        this.config = plugin.getConfig();
        this.accountFactory = plugin.getAccountFactory();
        this.accountStorage = plugin.getAccountStorage();
    }

    @Override
    public CompletableFuture<Account> onLogin(ProxyPlayer player) {
        String nickname = player.getNickname();
        if (!config.getNamePattern().matcher(nickname).matches()) {
            player.disconnect(config.getProxyMessages().getMessage("illegal-name-chars"));
            return CompletableFuture.completedFuture(null);
        }
        if (config.getMaxLoginPerIP() != 0 &&
                core.getPlayers().stream().filter(onlinePlayer -> onlinePlayer.getPlayerIp().equals(player.getPlayerIp())).count() >
                        config.getMaxLoginPerIP()) {
            player.disconnect(config.getProxyMessages().getMessage("limit-ip-reached"));
            return CompletableFuture.completedFuture(null);
        }
        String id = config.getActiveIdentifierType().getId(player);
        return accountStorage.getAccount(id).thenCompose(account -> {
            if (config.isNameCaseCheckEnabled() && account != null && !account.getName().equals(nickname)) {
                player.disconnect(config.getProxyMessages()
                        .getMessage("check-name-case-failed", MessageContext.of("%correct%", account.getName(), "%failed%", nickname)));
                return CompletableFuture.completedFuture(account);
            }

            if (account == null) {
                Account newAccount = accountFactory.createAccount(id, config.getActiveIdentifierType(), player.getUniqueId(), nickname,
                        config.getActiveHashType(), AccountFactory.DEFAULT_PASSWORD, AccountFactory.DEFAULT_GOOGLE_KEY, AccountFactory.DEFAULT_VK_ID,
                        AccountFactory.DEFAULT_VK_CONFIRMATION_STATE, AccountFactory.DEFAULT_LAST_QUIT, player.getPlayerIp(),
                        AccountFactory.DEFAULT_LAST_SESSION_START, config.getSessionDurability());

                AuthenticationStepContext context = plugin.getAuthenticationContextFactoryDealership().createContext(newAccount);
                Auth.addAccount(newAccount);
                newAccount.nextAuthenticationStep(context);
                return CompletableFuture.completedFuture(null);
            }

            AuthenticationStepCreator authenticationStepCreator = plugin.getAuthenticationStepCreatorDealership()
                    .findFirst(stepCreator -> stepCreator.getAuthenticationStepName()
                            .equals(plugin.getConfig().getAuthenticationSteps().stream().findFirst().orElse("NULL")))
                    .orElse(new NullAuthenticationStep.NullAuthenticationStepCreator());

            AuthenticationStepContext context = plugin.getAuthenticationContextFactoryDealership()
                    .createContext(authenticationStepCreator.getAuthenticationStepName(), account);

            if (account.isSessionActive(config.getSessionDurability())) {
                player.sendMessage(config.getProxyMessages().getMessage("autoconnect", new ProxyMessageContext(account)));
                if (config.getJoinDelay() == 0) {
                    account.nextAuthenticationStep(context);
                } else {
                    core.schedule(ProxyPlugin.instance(), () -> account.nextAuthenticationStep(context), config.getJoinDelay(), TimeUnit.MILLISECONDS);
                }
                return CompletableFuture.completedFuture(account);
            }

            if (Auth.hasAccount(account.getPlayerId()))
                throw new IllegalStateException("Cannot have two authenticating account at the same time!");

            Auth.addAccount(account);
            account.nextAuthenticationStep(context);
            return CompletableFuture.completedFuture(account);
        });
    }

    @Override
    public void onDisconnect(ProxyPlayer player) {
        String id = config.getActiveIdentifierType().getId(player);
        Auth.getLinkEntryAuth().removeLinkUsers(entryUser -> entryUser.getAccount().getPlayerId().equals(id));
        if (Auth.hasAccount(id)) {
            Auth.removeAccount(id);
            return;
        }
        accountStorage.getAccount(id).thenAccept(account -> {
            account.setLastQuitTimestamp(System.currentTimeMillis());
            accountStorage.saveOrUpdateAccount(account);
        });
    }
}
