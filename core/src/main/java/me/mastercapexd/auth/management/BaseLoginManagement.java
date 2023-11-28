package me.mastercapexd.auth.management;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

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
    public void onPreLogin(String username, Consumer<Boolean> continueConnection) {
        plugin.getAccountDatabase().getAccount(username).thenAccept(account -> {
            if (account == null) {
                continueConnection.accept(false);
                return;
            }

            if (account.isPremium()) {
                if (plugin.getPendingPremiumAccountBucket().isPendingPremium(account)) {
                    plugin.getPendingPremiumAccountBucket().removePendingPremiumAccount(account);
                }
                continueConnection.accept(true);
                return;
            }

            if (!plugin.getPendingPremiumAccountBucket().isPendingPremium(account)) {
                continueConnection.accept(false);
                return;
            }

            long timestamp = plugin.getPendingPremiumAccountBucket().getEnterTimestampOrZero(account);
            if (timestamp < System.currentTimeMillis() - plugin.getConfig().getLicenseVerifyTimeMillis()) {
                plugin.getPendingPremiumAccountBucket().removePendingPremiumAccount(account);
                continueConnection.accept(false);
                return;
            }

            continueConnection.accept(true);
        });
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
                        config.getActiveHashType(), null, player.getPlayerIp());

                AuthenticationStepContext context = plugin.getAuthenticationContextFactoryBucket(newAccount.isPremium()).createContext(newAccount);
                plugin.getAuthenticatingAccountBucket().addAuthenticatingAccount(newAccount);
                newAccount.nextAuthenticationStep(context);
                return CompletableFuture.completedFuture(null);
            }

            // We don't want to remove account from the bucket yet, we will do that in ServerConnected event,
            // after sending message and title
            if (plugin.getPendingPremiumAccountBucket().isPendingPremium(account)) {
                account.setIsPremium(true);
                accountDatabase.saveOrUpdateAccount(account);
            }

            AuthenticationStepFactory authenticationStepCreator = plugin.getAuthenticationStepFactoryBucket()
                    .findFirst(stepCreator -> stepCreator.getAuthenticationStepName()
                            .equals(plugin.getConfig().getAuthenticationSteps(account.isPremium()).stream().findFirst().orElse("NULL")))
                    .orElse(new NullAuthenticationStepFactory());

            AuthenticationStepContext context = plugin.getAuthenticationContextFactoryBucket(account.isPremium())
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
                        core.schedule(() -> account.nextAuthenticationStep(context), config.getJoinDelay(), TimeUnit.MILLISECONDS);
                    }
                    return account;
                }

                if (plugin.getAuthenticatingAccountBucket().isAuthenticating(account))
                    throw new IllegalStateException("Cannot have two authenticating account at the same time!");

                plugin.getAuthenticatingAccountBucket().addAuthenticatingAccount(account);
                account.nextAuthenticationStep(context);
                return account;
            });
        });
    }

    @Override
    public void onDisconnect(ServerPlayer player) {
        String id = config.getActiveIdentifierType().getId(player);
        plugin.getLinkEntryBucket().removeLinkUsers(entryUser -> entryUser.getAccount().getPlayerId().equals(id));
        long loginDuration = System.currentTimeMillis() - plugin.getAuthenticatingAccountBucket().getEnterTimestampOrZero(player);
        if (plugin.getAuthenticatingAccountBucket().isAuthenticating(player))
            return;
        accountDatabase.getAccount(id).thenAccept(account -> {
            account.setLastQuitTimestamp(System.currentTimeMillis());
            accountDatabase.saveOrUpdateAccount(account);
        });
    }
}
