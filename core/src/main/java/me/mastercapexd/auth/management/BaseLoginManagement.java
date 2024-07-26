package me.mastercapexd.auth.management;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.account.AccountFactory;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.config.message.MessageContext;
import com.bivashy.auth.api.config.premium.PremiumSettings;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.event.AccountJoinEvent;
import com.bivashy.auth.api.event.AccountSessionEnterEvent;
import com.bivashy.auth.api.event.result.PreLoginResult;
import com.bivashy.auth.api.factory.AuthenticationStepFactory;
import com.bivashy.auth.api.management.LoginManagement;
import com.bivashy.auth.api.premium.PremiumException;
import com.bivashy.auth.api.server.ServerCore;
import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.auth.api.server.player.ServerPlayer;
import com.bivashy.auth.api.step.AuthenticationStepContext;

import com.bivashy.auth.api.util.PasswordGenerator;
import io.github.revxrsal.eventbus.PostResult;
import me.mastercapexd.auth.config.message.server.ServerMessageContext;
import me.mastercapexd.auth.server.commands.impl.RegisterCommandImplementation;
import me.mastercapexd.auth.step.impl.NullAuthenticationStep.NullAuthenticationStepFactory;
import org.jetbrains.annotations.Nullable;

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
    public void onPreLogin(String ip, String username, Consumer<PreLoginResult> continuation) {
        ServerComponent invalidNicknameDisconnectMessage = validateNickname(username);
        if (invalidNicknameDisconnectMessage != null) {
            continuation.accept(new PreLoginResult(PreLoginResult.PreLoginState.DENIED, invalidNicknameDisconnectMessage));
        }

        PremiumSettings premiumSettings = config.getPremiumSettings();

        if (!premiumSettings.isEnabled()) {
            continuation.accept(new PreLoginResult(PreLoginResult.PreLoginState.FORCE_OFFLINE));
            return;
        }

        if (username.length() > 16) {
            onCrackedPreLogin(username, continuation);
            return;
        }

        if (plugin.getPendingLoginBucket().hasFailedLogin(ip, username)) {
            plugin.getPendingLoginBucket().removePendingLogin(ip, username);

            if (premiumSettings.getBlockOfflinePlayersWithPremiumName()) {
                continuation.accept(new PreLoginResult(
                        PreLoginResult.PreLoginState.DENIED,
                        config.getServerMessages().getMessage("offline-player-with-premium-name-blocked")
                ));
                return;
            }

            onCrackedPreLogin(username, continuation);
            return;
        }

        UUID premiumUuid = null;

        try {
            premiumUuid = plugin.getPremiumProvider().getPremiumUUIDForName(username);
        } catch (PremiumException e) {
            ServerComponent disconnectMessage;
            if (e.getIssue() == PremiumException.Issue.THROTTLED) {
                disconnectMessage = config.getServerMessages().getMessage("premium-server-throttled");
            } else {
                e.printStackTrace();
                disconnectMessage = config.getServerMessages().getMessage("premium-server-unknown-error");
            }
            continuation.accept(new PreLoginResult(PreLoginResult.PreLoginState.DENIED, disconnectMessage));
        }

        if (premiumUuid == null) {
            onCrackedPreLogin(username, continuation);
            return;
        }

        final UUID finalPremiumUuid = premiumUuid;
        accountDatabase.getAccountFromName(username).thenAccept(account -> {
            if (account == null) {
                plugin.getPendingLoginBucket().addPendingLogin(ip, username);
                continuation.accept(new PreLoginResult(PreLoginResult.PreLoginState.FORCE_ONLINE));
                return;
            }

            if (account.isPremium()) {
                continuation.accept(new PreLoginResult(PreLoginResult.PreLoginState.FORCE_ONLINE));
                return;
            }

            accountDatabase.getAccountFromUUID(finalPremiumUuid).thenAccept(premiumAccount -> {
                if (premiumAccount == null || !premiumAccount.isPremium()) {
                    continuation.accept(new PreLoginResult(PreLoginResult.PreLoginState.FORCE_OFFLINE));
                    return;
                }

                plugin.getPendingLoginBucket().addPendingLogin(ip, username);
                continuation.accept(new PreLoginResult(PreLoginResult.PreLoginState.FORCE_ONLINE));
            });
        });
    }

    private void onCrackedPreLogin(String username, Consumer<PreLoginResult> continuation) {
        accountDatabase.getAccountFromName(username).thenAccept(account -> {
            if (account == null || !account.isPremium()) {
                continuation.accept(new PreLoginResult(PreLoginResult.PreLoginState.FORCE_OFFLINE));
            } else {
                continuation.accept(new PreLoginResult(PreLoginResult.PreLoginState.FORCE_ONLINE));
            }
        });
    }

    @Override
    public CompletableFuture<Account> onLogin(ServerPlayer player) {
        if (config.getMaxLoginPerIP() != 0 &&
                core.getPlayers().stream().filter(onlinePlayer -> onlinePlayer.getPlayerIp().equals(player.getPlayerIp())).count() >
                        config.getMaxLoginPerIP()) {
            player.disconnect(config.getServerMessages().getMessage("limit-ip-reached"));
            return CompletableFuture.completedFuture(null);
        }

        if (player.isOnlineMode()) {
            return accountDatabase.getAccountFromUUID(player.getUniqueId()).thenCompose(premiumAccount -> {
                String id = config.getActiveIdentifierType().getId(player);
                return accountDatabase.getAccount(id).thenCompose(account -> {
                    if (premiumAccount == null) {
                        if (account == null && !config.getPremiumSettings().getAuthenticationSteps().contains("REGISTER")) {
                            return createAccountWithRandomPassword(id, player).thenCompose(password -> {
                                player.sendMessage(config.getServerMessages().getMessage("autogenerated-password",
                                        MessageContext.of("%password%", password)));
                                return handleLogin(player);
                            });
                        }
                        return handleLogin(player);
                    }

                    if (account == null) {
                        handlePremiumNickChange(player, premiumAccount);
                        return handleLogin(player);
                    }

                    if (!premiumAccount.getName().equals(account.getName())) {
                        return handleProfileConflict(player, account, premiumAccount).thenCompose(a -> handleLogin(player));
                    }

                    return handleLogin(player);
                });
            });
        }

        return handleLogin(player);
    }

    private CompletableFuture<Account> handleLogin(ServerPlayer player) {
        String nickname = player.getNickname();
        String id = config.getActiveIdentifierType().getId(player);
        return accountDatabase.getAccount(id).thenCompose(account -> {
            if (config.isNameCaseCheckEnabled() && account != null && !account.getName().equals(nickname)) {
                player.disconnect(config.getServerMessages()
                        .getMessage("check-name-case-failed", MessageContext.of("%correct%", account.getName(), "%failed%", nickname)));
                return CompletableFuture.completedFuture(account);
            }

            if (account == null) {
                Account newAccount = accountFactory.createAccount(id, config.getActiveIdentifierType(), player.getUniqueId(), nickname,
                        config.getActiveHashType(), null, player.getPlayerIp(), player.isOnlineMode());

                AuthenticationStepContext context = plugin.getAuthenticationContextFactoryBucket().createContext(newAccount);
                plugin.getAuthenticatingAccountBucket().addAuthenticatingAccount(newAccount);
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

    private CompletableFuture<String> createAccountWithRandomPassword(String id, ServerPlayer player) {
        Account account = accountFactory.createAccount(id, config.getActiveIdentifierType(), player.getUniqueId(),
                player.getNickname(), config.getActiveHashType(), null, player.getPlayerIp(), player.isOnlineMode());

        PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                .useDigits(true)
                .useLower(true)
                .useUpper(true)
                .build();
        String password = passwordGenerator.generate(12);

        return RegisterCommandImplementation.setAccountPassword(account, password).thenApply(a -> password);
    }

    private void handlePremiumNickChange(ServerPlayer player, Account account) {
        account.setName(player.getNickname());
        accountDatabase.saveOrUpdateAccount(account);
    }

    private CompletableFuture<Account> handleProfileConflict(ServerPlayer player, Account crackedAccount, Account premiumAccount) {
        switch (config.getPremiumSettings().getProfileConflictResolutionStrategy()) {
            case BLOCK:
                player.disconnect(config.getServerMessages().getMessage("profile-conflict-blocked"));
                break;
            case USE_OFFLINE:
                break;
            case OVERWRITE:
                return accountDatabase.deleteAccount(crackedAccount.getPlayerId()).thenCompose(v -> {
                    premiumAccount.setName(player.getNickname());
                    return accountDatabase.saveOrUpdateAccount(premiumAccount);
                });
        }

        return CompletableFuture.completedFuture(crackedAccount);
    }

    @Nullable
    private ServerComponent validateNickname(String nickname) {
        if (nickname.length() < config.getNameMinLength()) {
            return config.getServerMessages().getMessage("name-too-short");
        }

        if (nickname.length() > config.getNameMaxLength()) {
            return config.getServerMessages().getMessage("name-too-long");
        }

        if (!config.getNamePattern().matcher(nickname).matches()) {
            return config.getServerMessages().getMessage("illegal-name-chars");
        }

        return null;
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
