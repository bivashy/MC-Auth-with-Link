package me.mastercapexd.auth.velocity.listener;

import java.util.Optional;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;

public class AuthenticationListener {
    private final ProxyPlugin plugin;
    private final PluginConfig config;

    public AuthenticationListener(ProxyPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @Subscribe
    public void onPostLoginEvent(PostLoginEvent event) {
        plugin.getCore().wrapPlayer(event.getPlayer()).ifPresent(plugin.getLoginManagement()::onLogin);
    }

    @Subscribe
    public void onPlayerLeave(DisconnectEvent event) {
        plugin.getCore().wrapPlayer(event.getPlayer()).ifPresent(plugin.getLoginManagement()::onDisconnect);
    }

    @Subscribe
    public void onChatEvent(PlayerChatEvent event) {
        if (!event.getResult().isAllowed())
            return;
        ProxyPlayer player = plugin.getCore().wrapPlayer(event.getPlayer()).get();
        if (!Auth.hasAccount(config.getActiveIdentifierType().getId(player)))
            return;
        if (!config.shouldBlockChat())
            return;
        player.sendMessage(config.getProxyMessages().getStringMessage("disabled-chat"));
        event.setResult(PlayerChatEvent.ChatResult.denied());
    }

    @Subscribe
    public void onCommandEvent(CommandExecuteEvent event) {
        if (!event.getResult().isAllowed())
            return;
        Optional<ProxyPlayer> proxyPlayerOptional = plugin.getCore().wrapPlayer(event.getCommandSource());
        if (!proxyPlayerOptional.isPresent())
            return;
        ProxyPlayer player = proxyPlayerOptional.get();
        if (!Auth.hasAccount(config.getActiveIdentifierType().getId(player)))
            return;
        if (config.getAllowedCommands().stream().anyMatch(pattern -> pattern.matcher(event.getCommand()).find()))
            return;
        player.sendMessage(config.getProxyMessages().getStringMessage("disabled-chat"));
        event.setResult(CommandExecuteEvent.CommandResult.denied());
    }


    @Subscribe
    public void onBlockedServerConnect(ServerPreConnectEvent event) {
        if (!event.getResult().getServer().isPresent())
            return;
        ProxyPlayer player = plugin.getCore().wrapPlayer(event.getPlayer()).get();
        String id = config.getActiveIdentifierType().getId(player);
        if (!Auth.hasAccount(id))
            return;
        if (config.getBlockedServers().stream().noneMatch(server -> event.getResult().getServer().get().getServerInfo().getName().equals(server.getId())))
            return;
        player.sendMessage(config.getProxyMessages().getStringMessage("disabled-server"));
        event.setResult(ServerPreConnectEvent.ServerResult.denied());
    }
}