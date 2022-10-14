package me.mastercapexd.auth.bungee.listener;

import java.util.Optional;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.bungee.player.BungeeProxyPlayer;
import me.mastercapexd.auth.bungee.server.BungeeServer;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class AuthenticationListener implements Listener {
    private final ProxyPlugin plugin;

    public AuthenticationListener(ProxyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        plugin.getCore().wrapPlayer(event.getPlayer()).ifPresent(plugin.getLoginManagement()::onLogin);
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent event) {
        plugin.getCore().wrapPlayer(event.getPlayer()).ifPresent(plugin.getLoginManagement()::onDisconnect);
    }

    @EventHandler
    public void onPlayerChat(ChatEvent event) {
        if (event.isCancelled())
            return;
        Optional<ProxyPlayer> playerOptional = plugin.getCore().wrapPlayer(event.getSender());
        if (!playerOptional.isPresent())
            return;
        ProxyPlayer player = playerOptional.get();
        if (!Auth.hasAccount(plugin.getConfig().getActiveIdentifierType().getId(player)))
            return;
        if (plugin.getConfig().shouldBlockChat() && !event.isProxyCommand()) {
            player.sendMessage(plugin.getConfig().getProxyMessages().getStringMessage("disabled-chat"));
            event.setCancelled(true);
            return;
        }
        if (plugin.getConfig().getAllowedCommands().stream().anyMatch(pattern -> pattern.matcher(event.getMessage()).find()))
            return;
        player.sendMessage(plugin.getConfig().getProxyMessages().getStringMessage("disabled-command"));
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockedServerConnect(ServerConnectEvent event) {
        ProxyPlayer player = new BungeeProxyPlayer(event.getPlayer());
        String id = plugin.getConfig().getActiveIdentifierType().getId(player);
        if (!Auth.hasAccount(id))
            return;
        if (config.getBlockedServers().stream().noneMatch(server -> event.getTarget().getName().equals(server.getId())))
            return;
        }
        player.sendMessage(plugin.getConfig().getProxyMessages().getStringMessage("disabled-server"));
        event.setCancelled(true);
    }
}