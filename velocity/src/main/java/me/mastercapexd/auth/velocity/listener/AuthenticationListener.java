package me.mastercapexd.auth.velocity.listener;

import java.util.Optional;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.server.player.ServerPlayer;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent.ServerResult;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import me.mastercapexd.auth.velocity.server.VelocityProxyServer;

public class AuthenticationListener {
    private final AuthPlugin plugin;

    public AuthenticationListener(AuthPlugin plugin) {
        this.plugin = plugin;
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
        plugin.getCore().wrapPlayer(event.getPlayer()).ifPresent(player -> {
            if (!plugin.getAuthenticatingAccountBucket().isAuthenticating(player))
                return;
            if (!plugin.getConfig().shouldBlockChat() || event.getResult().getMessage().orElse("").startsWith("/"))
                return;
            player.sendMessage(plugin.getConfig().getServerMessages().getMessage("disabled-chat"));
            event.setResult(PlayerChatEvent.ChatResult.denied());
        });
    }

    @Subscribe
    public void onCommandEvent(CommandExecuteEvent event) {
        if (!event.getResult().isAllowed())
            return;
        Optional<ServerPlayer> proxyPlayerOptional = plugin.getCore().wrapPlayer(event.getCommandSource());
        if (!proxyPlayerOptional.isPresent())
            return;
        ServerPlayer player = proxyPlayerOptional.get();
        if (!plugin.getAuthenticatingAccountBucket().isAuthenticating(player))
            return;
        String command = "/" + event.getCommand();
        if (plugin.getConfig().getAllowedCommands().stream().anyMatch(pattern -> pattern.matcher(command).find()))
            return;
        player.sendMessage(plugin.getConfig().getServerMessages().getMessage("disabled-command"));
        event.setResult(CommandExecuteEvent.CommandResult.denied());
    }

    @Subscribe
    public void onBlockedServerConnect(ServerPreConnectEvent event) {
        plugin.getCore().wrapPlayer(event.getPlayer()).ifPresent(player -> {
            String id = plugin.getConfig().getActiveIdentifierType().getId(player);
            if (!plugin.getAuthenticatingAccountBucket().isAuthenticating(player))
                return;
            Optional<RegisteredServer> resultServerOptional = event.getResult().getServer();
            if (!resultServerOptional.isPresent())
                return;
            if (plugin.getConfig()
                    .getBlockedServers()
                    .stream()
                    .noneMatch(server -> resultServerOptional.get().getServerInfo().getName().equals(server.getId())))
                return;
            if (!event.getResult().getServer().isPresent()) {
                event.setResult(ServerResult.allowed(
                        plugin.getConfig().findServerInfo(plugin.getConfig().getAuthServers()).asProxyServer().as(VelocityProxyServer.class).getServer()));
                return;
            }

            player.sendMessage(plugin.getConfig().getServerMessages().getMessage("disabled-server"));
            event.setResult(ServerPreConnectEvent.ServerResult.denied());
        });
    }
}