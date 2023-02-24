package me.mastercapexd.auth.bungee.listener;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.bungee.player.BungeeConnectionProxyPlayer;
import me.mastercapexd.auth.bungee.player.BungeeProxyPlayer;
import me.mastercapexd.auth.bungee.server.BungeeServer;
import me.mastercapexd.auth.config.message.proxy.ProxyMessageContext;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class AuthenticationListener implements Listener {
    private static final Set<UUID> INVALID_ACCOUNTS = new HashSet<>();
    private final ProxyPlugin plugin;

    public AuthenticationListener(ProxyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLogin(LoginEvent event) {
        event.registerIntent(plugin.as(AuthPlugin.class));
        plugin.getLoginManagement().onLogin(new BungeeConnectionProxyPlayer(event.getConnection())).whenComplete((account, exception) -> {
            if (exception != null)
                INVALID_ACCOUNTS.add(event.getConnection().getUniqueId());
            event.completeIntent(plugin.as(AuthPlugin.class));
            if (account == null)
                return;
            // Using dirty way because we cannot send message in LoginEvent
            plugin.getCore()
                    .schedule(plugin, () -> account.getPlayer()
                            .ifPresent(player -> player.sendMessage(
                                    plugin.getConfig().getProxyMessages().getMessage("autoconnect", new ProxyMessageContext(account)))), 1, TimeUnit.SECONDS);
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
        Optional<ProxyPlayer> playerOptional = plugin.getCore().wrapPlayer(event.getSender());
        if (!playerOptional.isPresent())
            return;
        ProxyPlayer player = playerOptional.get();
        if (!plugin.getAuthenticatingAccountBucket().isAuthorizing(player))
            return;
        if (plugin.getConfig().shouldBlockChat() && !event.isCommand()) {
            player.sendMessage(plugin.getConfig().getProxyMessages().getMessage("disabled-chat"));
            event.setCancelled(true);
            return;
        }
        if (plugin.getConfig().getAllowedCommands().stream().anyMatch(pattern -> pattern.matcher(event.getMessage()).find()))
            return;
        player.sendMessage(plugin.getConfig().getProxyMessages().getMessage("disabled-command"));
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockedServerConnect(ServerConnectEvent event) {
        ProxyPlayer player = new BungeeProxyPlayer(event.getPlayer());
        if (!plugin.getAuthenticatingAccountBucket().isAuthorizing(player))
            return;
        if (plugin.getConfig().getBlockedServers().stream().noneMatch(server -> event.getTarget().getName().equals(server.getId()))) {
            event.setTarget(plugin.getConfig().findServerInfo(plugin.getConfig().getAuthServers()).asProxyServer().as(BungeeServer.class).getServerInfo());
            return;
        }
        player.sendMessage(plugin.getConfig().getProxyMessages().getMessage("disabled-server"));
        event.setCancelled(true);
    }
}