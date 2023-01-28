package me.mastercapexd.auth.proxy.commands;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.storage.AccountStorage;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;

@Command("logout")
public class LogoutCommand {
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountStorage accountStorage;

    @Default
    public void logout(ProxyPlayer player) {
        String id = config.getActiveIdentifierType().getId(player);
        if (Auth.hasAccount(id)) {
            player.sendMessage(config.getProxyMessages().getMessage("already-logged-out"));
            return;
        }
        accountStorage.getAccount(id).thenAccept(account -> {
            account.logout(config.getSessionDurability());
            accountStorage.saveOrUpdateAccount(account);
            Auth.addAccount(account);
            player.sendMessage(config.getProxyMessages().getMessage("logout-success"));
            player.sendTo(config.findServerInfo(config.getAuthServers()).asProxyServer());
        });
    }
}