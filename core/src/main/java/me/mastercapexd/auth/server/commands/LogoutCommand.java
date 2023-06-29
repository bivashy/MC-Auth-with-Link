package me.mastercapexd.auth.server.commands;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.server.player.ServerPlayer;

import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Dependency;

@Command("logout")
public class LogoutCommand {
    @Dependency
    private AuthPlugin plugin;
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountDatabase accountStorage;

    @DefaultFor("logout")
    public void logout(ServerPlayer player) {
        String id = config.getActiveIdentifierType().getId(player);
        if (plugin.getAuthenticatingAccountBucket().isAuthenticating(player)) {
            player.sendMessage(config.getServerMessages().getMessage("already-logged-out"));
            return;
        }
        accountStorage.getAccount(id).thenAccept(account -> {
            account.logout(config.getSessionDurability());
            accountStorage.saveOrUpdateAccount(account);
            plugin.getAuthenticatingAccountBucket().addAuthenticatingAccount(account);
            player.sendMessage(config.getServerMessages().getMessage("logout-success"));
            config.findServerInfo(config.getAuthServers()).asProxyServer().sendPlayer(player);
        });
    }
}