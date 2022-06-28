package me.mastercapexd.auth.bungee.listener;

import java.util.regex.Pattern;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.bungee.player.BungeeProxyPlayer;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class AuthenticationListener implements Listener {

    private final ProxyPlugin plugin;
    private final PluginConfig config;

    public AuthenticationListener(ProxyPlugin plugin, PluginConfig config) {
        this.plugin = plugin;
        this.config = config;
    }

    @EventHandler
    public void onServerConnected(PostLoginEvent event) {
        plugin.getCore().wrapPlayer(event.getPlayer()).ifPresent(plugin.getLoginManagement()::onLogin);
    }

    @EventHandler
    public void onPlayerChat(ChatEvent event) {
        if (event.isCancelled())
            return;
        if (!(event.getSender() instanceof ProxiedPlayer))
            return;
        ProxyPlayer player = new BungeeProxyPlayer((ProxiedPlayer) event.getSender());
        if (!Auth.hasAccount(config.getActiveIdentifierType().getId(player)))
            return;

        String message = event.getMessage();
        if (!isAllowedCommand(message)) {
            player.sendMessage(config.getProxyMessages().getStringMessage("disabled-command"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockedServerConnect(ServerConnectEvent event) {
        ProxyPlayer player = new BungeeProxyPlayer(event.getPlayer());
        String id = config.getActiveIdentifierType().getId(player);
        if (!(Auth.hasAccount(id)))
            return;
        if (config.getBlockedServers().stream().noneMatch(server -> event.getTarget().getName().equals(server.getId())))
            return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent event) {
        plugin.getCore().wrapPlayer(event.getPlayer()).ifPresent(plugin.getLoginManagement()::onDisconnect);
    }

    private boolean isAllowedCommand(String command) {
        return config.getAllowedCommands().stream().map(Pattern::compile).anyMatch(pattern -> pattern.matcher(command).find());
    }

}