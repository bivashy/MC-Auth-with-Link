package me.mastercapexd.auth.bungee.listener;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.event.PlayerChatPasswordEvent;
import com.bivashy.auth.api.server.player.ServerPlayer;

import me.mastercapexd.auth.bungee.BungeeAuthPluginBootstrap;
import me.mastercapexd.auth.bungee.message.BungeeComponent;
import me.mastercapexd.auth.bungee.player.BungeeConnectionProxyPlayer;
import me.mastercapexd.auth.bungee.player.BungeeServerPlayer;
import me.mastercapexd.auth.bungee.server.BungeeServer;
import me.mastercapexd.auth.config.message.server.ServerMessageContext;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class AuthenticationListener implements Listener {
    private static final Set<UUID> INVALID_ACCOUNTS = new HashSet<>();
    private final BungeeAuthPluginBootstrap bungeePlugin = BungeeAuthPluginBootstrap.getInstance();
    private final AuthPlugin plugin;

    public AuthenticationListener(AuthPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPreLogin(PreLoginEvent event) {
        event.registerIntent(bungeePlugin);
        plugin.getLoginManagement().onPreLogin(event.getConnection().getAddress().getAddress().getHostAddress(), event.getConnection().getName(), result -> {
            switch (result.getState()) {
                case DENIED:
                    ServerPlayer p = new BungeeConnectionProxyPlayer(event.getConnection());
                    p.disconnect(result.getDisconnectMessage());
                    event.setCancelled(true);
                    break;
                case FORCE_OFFLINE:
                    event.getConnection().setOnlineMode(false);
                    break;
                case FORCE_ONLINE:
                    event.getConnection().setOnlineMode(true);
                    break;
            }

            event.completeIntent(bungeePlugin);
        });
    }

    @EventHandler
    public void onLogin(LoginEvent event) {
        event.registerIntent(bungeePlugin);
        ServerPlayer connectionPlayer = new BungeeConnectionProxyPlayer(event.getConnection());
        plugin.getLoginManagement().onLogin(connectionPlayer).whenComplete((account, exception) -> {
            if (exception != null)
                INVALID_ACCOUNTS.add(event.getConnection().getUniqueId());
            event.completeIntent(bungeePlugin);
            if (account == null)
                return;
            // Using dirty way because we cannot send message in LoginEvent
            plugin.getCore().schedule(() -> {
                if (plugin.getAuthenticatingAccountBucket().isAuthenticating(connectionPlayer))
                    return;
                account.getPlayer()
                        .ifPresent(player -> player.sendMessage(
                                plugin.getConfig().getServerMessages().getMessage("autoconnect", new ServerMessageContext(account))));
            }, 1, TimeUnit.SECONDS);
        });
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent event) {
        if (INVALID_ACCOUNTS.remove(event.getPlayer().getUniqueId()))
            return;
        plugin.getCore().wrapPlayer(event.getPlayer()).ifPresent(plugin.getLoginManagement()::onDisconnect);
    }

    @EventHandler
    public void onPlayerChat(ChatEvent event) {
        if (event.isCancelled())
            return;
        Optional<ServerPlayer> playerOptional = plugin.getCore().wrapPlayer(event.getSender());
        if (!playerOptional.isPresent())
            return;
        ServerPlayer player = playerOptional.get();
        if (!plugin.getAuthenticatingAccountBucket().isAuthenticating(player))
            return;

        if (plugin.getConfig().isPasswordInChatEnabled() && !event.isCommand()) {
            plugin.getEventBus().publish(PlayerChatPasswordEvent.class, player, event.getMessage());
            event.setCancelled(true);
            return;
        }

        if (plugin.getConfig().shouldBlockChat() && !event.isCommand()) {
            player.sendMessage(plugin.getConfig().getServerMessages().getMessage("disabled-chat"));
            event.setCancelled(true);
            return;
        }

        if (plugin.getConfig().getAllowedCommands().stream().anyMatch(pattern -> pattern.matcher(event.getMessage()).find()))
            return;
        player.sendMessage(plugin.getConfig().getServerMessages().getMessage("disabled-command"));
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockedServerConnect(ServerConnectEvent event) {
        ServerPlayer player = new BungeeServerPlayer(event.getPlayer());
        if (!plugin.getAuthenticatingAccountBucket().isAuthenticating(player))
            return;
        if (plugin.getConfig().getBlockedServers().stream().noneMatch(server -> event.getTarget().getName().equals(server.getId()))) {
            event.setTarget(plugin.getConfig().findServerInfo(plugin.getConfig().getAuthServers()).asProxyServer().as(BungeeServer.class).getServerInfo());
            return;
        }
        player.sendMessage(plugin.getConfig().getServerMessages().getMessage("disabled-server"));
        event.setCancelled(true);
    }
}