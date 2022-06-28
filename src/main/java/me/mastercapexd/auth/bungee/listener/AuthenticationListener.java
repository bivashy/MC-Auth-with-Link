package me.mastercapexd.auth.bungee.listener;

import java.util.Optional;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.bungee.player.BungeeProxyPlayer;
import me.mastercapexd.auth.config.PluginConfig;
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
    private final PluginConfig config;

    public AuthenticationListener(ProxyPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
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
        if (!Auth.hasAccount(config.getActiveIdentifierType().getId(player)))
            return;
        if (config.shouldBlockChat()) {
            player.sendMessage(config.getProxyMessages().getStringMessage("disabled-chat"));
            event.setCancelled(true);
            return;
        }
        if (config.getAllowedCommands().stream().anyMatch(pattern -> pattern.matcher(event.getMessage()).find()))
            return;
        player.sendMessage(config.getProxyMessages().getStringMessage("disabled-command"));
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockedServerConnect(ServerConnectEvent event) {
        ProxyPlayer player = new BungeeProxyPlayer(event.getPlayer());
        String id = config.getActiveIdentifierType().getId(player);
        if (!Auth.hasAccount(id))
            return;
        if (config.getBlockedServers().stream().noneMatch(server -> event.getTarget().getName().equals(server.getId())))
            return;
        player.sendMessage(config.getProxyMessages().getStringMessage("disabled-server"));
        event.setCancelled(true);
    }
}