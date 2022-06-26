package me.mastercapexd.auth.bungee;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import me.mastercapexd.auth.bungee.api.bossbar.BungeeProxyBossbar;
import me.mastercapexd.auth.bungee.api.title.BungeeProxyTitle;
import me.mastercapexd.auth.bungee.message.BungeeMultiProxyComponent;
import me.mastercapexd.auth.bungee.player.BungeeProxyPlayer;
import me.mastercapexd.auth.bungee.server.BungeeServer;
import me.mastercapexd.auth.proxy.ProxyCore;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.api.bossbar.ProxyBossbar;
import me.mastercapexd.auth.proxy.api.title.ProxyTitle;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.proxy.server.Server;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.chat.ComponentSerializer;

public enum BungeeProxyCore implements ProxyCore {
    INSTANCE;

    private static final ProxyServer PROXY_SERVER = ProxyServer.getInstance();
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    @Override
    public <E> void callEvent(E event) {
        PROXY_SERVER.getPluginManager().callEvent((Event) event);
    }

    @Override
    public Optional<ProxyPlayer> getPlayer(UUID uniqueId) {
        ProxiedPlayer proxiedPlayer = PROXY_SERVER.getPlayer(uniqueId);
        if (proxiedPlayer == null)
            return Optional.empty();
        return Optional.of(new BungeeProxyPlayer(proxiedPlayer));
    }

    @Override
    public Optional<ProxyPlayer> getPlayer(String name) {
        ProxiedPlayer proxiedPlayer = PROXY_SERVER.getPlayer(name);
        if (proxiedPlayer == null)
            return Optional.empty();
        return Optional.of(new BungeeProxyPlayer(proxiedPlayer));
    }

    @Override
    public Optional<ProxyPlayer> wrapPlayer(Object player) {
        if (player == null)
            return Optional.empty();
        if (player instanceof ProxyPlayer)
            return Optional.of((ProxyPlayer) player);
        if (player instanceof ProxiedPlayer)
            return Optional.of(new BungeeProxyPlayer((ProxiedPlayer) player));
        return Optional.empty();
    }

    @Override
    public Logger getLogger() {
        return PROXY_SERVER.getLogger();
    }

    @Override
    public ProxyTitle createTitle(String title) {
        return new BungeeProxyTitle().title(title);
    }

    @Override
    public ProxyBossbar createBossbar(String title) {
        return new BungeeProxyBossbar(title);
    }

    @Override
    public ProxyComponent componentPlain(String plain) {
        return new BungeeMultiProxyComponent(ChatColor.stripColor(plain));
    }

    @Override
    public ProxyComponent componentJson(String json) {
        return new BungeeMultiProxyComponent(ComponentSerializer.parse(json));
    }

    @Override
    public ProxyComponent componentLegacy(String legacy) {
        return new BungeeMultiProxyComponent(legacy);
    }

    @Override
    public Optional<Server> serverFromName(String serverName) {
        ServerInfo serverInfo = PROXY_SERVER.getServerInfo(serverName);
        if (serverInfo == null)
            return Optional.empty();
        return Optional.of(new BungeeServer(serverInfo));
    }

    @Override
    public void registerListener(ProxyPlugin plugin, Object listener) {
        PROXY_SERVER.getPluginManager().registerListener(plugin.as(AuthPlugin.class), (Listener) listener);
    }

    @Override
    public void schedule(ProxyPlugin plugin, Runnable task, long delay, long period, TimeUnit unit) {
        PROXY_SERVER.getScheduler().schedule(plugin.as(AuthPlugin.class), task, delay, period, unit);
    }

    @Override
    public void runAsync(Runnable task) {
        EXECUTOR_SERVICE.execute(task);
    }

}
