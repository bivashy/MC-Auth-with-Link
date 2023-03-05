package me.mastercapexd.auth.management;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.account.AccountFactory;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.config.message.MessageContext;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.event.AccountJoinEvent;
import com.bivashy.auth.api.event.AccountSessionEnterEvent;
import com.bivashy.auth.api.factory.AuthenticationStepFactory;
import com.bivashy.auth.api.management.LoginManagement;
import com.bivashy.auth.api.server.ServerCore;
import com.bivashy.auth.api.server.player.ServerPlayer;
import com.bivashy.auth.api.step.AuthenticationStepContext;

import io.github.revxrsal.eventbus.PostResult;
import me.mastercapexd.auth.config.message.server.ServerMessageContext;
import me.mastercapexd.auth.step.impl.NullAuthenticationStep.NullAuthenticationStepFactory;

public class BaseLoginManagement implements LoginManagement {
    private final AuthPlugin plugin;
    private final ServerCore core;
    private final PluginConfig config;
    private final AccountFactory accountFactory;
    private final AccountDatabase accountDatabase;

    public BaseLoginManagement(AuthPlugin plugin) {
        this.plugin = plugin;
        this.core = plugin.getCore();
        this.config = plugin.getConfig();
        this.accountFactory = plugin.getAccountFactory();
        this.accountDatabase = plugin.getAccountDatabase();
    }

    @Override
    public CompletableFuture<Account> onLogin(ServerPlayer player) {
        String nickname = player.getNickname();
        if (!config.getNamePattern().matcher(nickname).matches()) {
            player.disconnect(config.getServerMessages().getMessage("illegal-name-chars"));
            return CompletableFuture.completedFuture(null);
        }
        if (config.getMaxLoginPerIP() != 0 &&
                core.getPlayers().stream().filter(onlinePlayer -> onlinePlayer.getPlayerIp().equals(player.getPlayerIp())).count() >
                        config.getMaxLoginPerIP()) {
            player.disconnect(config.getServerMessages().getMessage("limit-ip-reached"));
            return CompletableFuture.completedFuture(null);
        }
        String id = config.getActiveIdentifierType().getId(player);
        return accountDatabase.getAccount(id).thenCompose(account -> {
            if (config.isNameCaseCheckEnabled() && account != null && !account.getName().equals(nickname)) {
                player.disconnect(config.getServerMessages()
                        .getMessage("check-name-case-failed", MessageContext.of("%correct%", account.getName(), "%failed%", nickname)));
                return CompletableFuture.completedFuture(account);
            }

            if (account == null) {
                Account newAccount = accountFactory.createAccount(id, config.getActiveIdentifierType(), player.getUniqueId(), nickname,
                        config.getActiveHashType(), AccountFactory.DEFAULT_PASSWORD, AccountFactory.DEFAULT_GOOGLE_KEY, AccountFactory.DEFAULT_VK_ID,
                        AccountFactory.DEFAULT_VK_CONFIRMATION_STATE, AccountFactory.DEFAULT_TELEGRAM_ID, AccountFactory.DEFAULT_TELEGRAM_CONFIRMATION_STATE,
                        AccountFactory.DEFAULT_LAST_QUIT, player.getPlayerIp(), AccountFactory.DEFAULT_LAST_SESSION_START, config.getSessionDurability());

                AuthenticationStepContext context = plugin.getAuthenticationContextFactoryBucket().createContext(newAccount);
                plugin.getAuthenticatingAccountBucket().addAuthorizingAccount(newAccount);
                newAccount.nextAuthenticationStep(context);
                return CompletableFuture.completedFuture(null);
            }

            AuthenticationStepFactory authenticationStepCreator = plugin.getAuthenticationStepFactoryBucket()
                    .findFirst(stepCreator -> stepCreator.getAuthenticationStepName()
                            .equals(plugin.getConfig().getAuthenticationSteps().stream().findFirst().orElse("NULL")))
                    .orElse(new NullAuthenticationStepFactory());

            AuthenticationStepContext context = plugin.getAuthenticationContextFactoryBucket()
                    .createContext(authenticationStepCreator.getAuthenticationStepName(), account);

            return plugin.getEventBus().publish(AccountJoinEvent.class, account, false).thenApplyAsync(event -> {
                if (event.getEvent().isCancelled())
                    return account;
                if (account.isSessionActive(config.getSessionDurability()) && account.getLastIpAddress().equals(player.getPlayerIp())) {
                    PostResult<AccountSessionEnterEvent> sessionEnterEventPostResult = plugin.getEventBus()
                            .publish(AccountSessionEnterEvent.class, account, false)
                            .join();
                    if (sessionEnterEventPostResult.getEvent().isCancelled())
                        return account;
                    player.sendMessage(config.getServerMessages().getMessage("autoconnect", new ServerMessageContext(account)));
                    if (config.getJoinDelay() == 0) {
                        account.nextAuthenticationStep(context);
                    } else {
                        core.schedule(AuthPlugin.instance(), () -> account.nextAuthenticationStep(context), config.getJoinDelay(), TimeUnit.MILLISECONDS);
                    }
                    return account;
                }

                if (plugin.getAuthenticatingAccountBucket().isAuthorizing(account))
                    throw new IllegalStateException("Cannot have two authenticating account at the same time!");

                plugin.getAuthenticatingAccountBucket().addAuthorizingAccount(account);
                account.nextAuthenticationStep(context);
                return account;
            });
        });
    }

    @Override
    public void onDisconnect(ServerPlayer player) {
        String id = config.getActiveIdentifierType().getId(player);
        plugin.getLinkEntryBucket().removeLinkUsers(entryUser -> entryUser.getAccount().getPlayerId().equals(id));
        if (plugin.getAuthenticatingAccountBucket().isAuthorizing(player)) {
            plugin.getAuthenticatingAccountBucket().removeAuthorizingAccount(player);
            return;
        }
        accountDatabase.getAccount(id).thenAccept(account -> {
            account.setLastQuitTimestamp(System.currentTimeMillis());
            accountDatabase.saveOrUpdateAccount(account);
        });
    }
}
