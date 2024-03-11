package me.mastercapexd.auth.server.commands;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.bucket.AuthenticatingAccountBucket;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.event.PlayerLogoutEvent;
import com.bivashy.auth.api.server.player.ServerPlayer;

import io.github.revxrsal.eventbus.EventBus;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Dependency;

@Command("logout")
public class LogoutCommand {
    @Dependency
    private AuthenticatingAccountBucket authenticatingAccountBucket;
    @Dependency
    private EventBus eventBus;
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountDatabase accountStorage;

    @DefaultFor("logout")
    public void logout(ServerPlayer player) {
        String id = config.getActiveIdentifierType().getId(player);
        if (authenticatingAccountBucket.isAuthenticating(player)) {
            player.sendMessage(config.getServerMessages().getMessage("already-logged-out"));
            return;
        }
        accountStorage.getAccount(id).thenAccept(account -> {
            eventBus.publish(PlayerLogoutEvent.class, player, false).thenAccept(result -> {
                if (result.getEvent().isCancelled())
                    return;
                account.logout(config.getSessionDurability());
                accountStorage.saveOrUpdateAccount(account);
                authenticatingAccountBucket.addAuthenticatingAccount(account);
                if (account.isPremium()) {
                    account.nextAuthenticationStep(AuthPlugin.instance().getPremiumAuthenticationContextFactoryBucket().createContext(account));
                } else {
                    account.nextAuthenticationStep(AuthPlugin.instance().getAuthenticationContextFactoryBucket().createContext(account));
                }
                player.sendMessage(config.getServerMessages().getMessage("logout-success"));
                config.findServerInfo(config.getAuthServers()).asProxyServer().sendPlayer(player);
            });
        });
    }
}