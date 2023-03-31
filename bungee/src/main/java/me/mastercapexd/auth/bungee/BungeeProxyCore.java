package me.mastercapexd.auth.bungee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.server.ServerCore;
import com.bivashy.auth.api.server.bossbar.ServerBossbar;
import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.auth.api.server.player.ServerPlayer;
import com.bivashy.auth.api.server.proxy.limbo.LimboServerWrapper;
import com.bivashy.auth.api.server.scheduler.ServerScheduler;
import com.bivashy.auth.api.server.title.ServerTitle;

import me.mastercapexd.auth.bungee.api.bossbar.BungeeServerBossbar;
import me.mastercapexd.auth.bungee.api.title.BungeeServerTitle;
import me.mastercapexd.auth.bungee.message.BungeeComponent;
import me.mastercapexd.auth.bungee.message.BungeeServerComponent;
import me.mastercapexd.auth.bungee.player.BungeeServerPlayer;
import me.mastercapexd.auth.bungee.scheduler.BungeeSchedulerWrapper;
import me.mastercapexd.auth.bungee.server.BungeeServer;
import me.mastercapexd.auth.hooks.limbo.LimboHook;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.chat.ComponentSerializer;

public enum BungeeProxyCore implements ServerCore {
    INSTANCE;
    private static final ProxyServer PROXY_SERVER = ProxyServer.getInstance();
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    @Override
    public <E> void callEvent(E event) {
        PROXY_SERVER.getPluginManager().callEvent((Event) event);
    }

    @Override
    public List<ServerPlayer> getPlayers() {
        return PROXY_SERVER.getPlayers().stream().map(BungeeServerPlayer::new).collect(Collectors.toList());
    }

    @Override
    public Optional<ServerPlayer> getPlayer(UUID uniqueId) {
        ProxiedPlayer proxiedPlayer = PROXY_SERVER.getPlayer(uniqueId);
        if (proxiedPlayer == null)
            return Optional.empty();
        return Optional.of(new BungeeServerPlayer(proxiedPlayer));
    }

    @Override
    public Optional<ServerPlayer> getPlayer(String name) {
        ProxiedPlayer proxiedPlayer = PROXY_SERVER.getPlayer(name);
        if (proxiedPlayer == null)
            return Optional.empty();
        return Optional.of(new BungeeServerPlayer(proxiedPlayer));
    }

    @Override
    public Optional<ServerPlayer> wrapPlayer(Object player) {
        if (player == null)
            return Optional.empty();
        if (player instanceof ServerPlayer)
            return Optional.of((ServerPlayer) player);
        if (player instanceof ProxiedPlayer)
            return Optional.of(new BungeeServerPlayer((ProxiedPlayer) player));
        return Optional.empty();
    }

    @Override
    public Logger getLogger() {
        return PROXY_SERVER.getLogger();
    }

    @Override
    public ServerTitle createTitle(ServerComponent title) {
        return new BungeeServerTitle(title);
    }

    @Override
    public ServerBossbar createBossbar(ServerComponent component) {
        return new BungeeServerBossbar(component.jsonText());
    }

    @Override
    public ServerComponent componentPlain(String plain) {
        return new BungeeServerComponent(ChatColor.stripColor(plain));
    }

    @Override
    public ServerComponent componentJson(String json) {
        return new BungeeServerComponent(ComponentSerializer.parse(json));
    }

    @Override
    public ServerComponent componentLegacy(String legacy) {
        return new BungeeServerComponent(legacy);
    }

    @Override
    public Optional<com.bivashy.auth.api.server.proxy.ProxyServer> serverFromName(String serverName) {
        ServerInfo serverInfo = PROXY_SERVER.getServerInfo(serverName);
        LimboHook limboHook = AuthPlugin.instance().getHook(LimboHook.class);
        if (serverInfo == null && limboHook != null) {
            if (!limboHook.isLimbo(serverName))
                return Optional.empty();
            LimboServerWrapper server = limboHook.createLimboWrapper(serverName);
            if (!server.isExists())
                return Optional.empty();
            return Optional.of(server);
        }
        return Optional.of(new BungeeServer(serverInfo));
    }

    @Override
    public void registerListener(AuthPlugin plugin, Object listener) {
        PROXY_SERVER.getPluginManager().registerListener(BungeeAuthPluginBootstrap.getInstance(), (Listener) listener);
    }

    @Override
    public ServerScheduler schedule(Runnable task, long delay, long period, TimeUnit unit) {
        return new BungeeSchedulerWrapper(PROXY_SERVER.getScheduler().schedule(BungeeAuthPluginBootstrap.getInstance(), task, delay, period, unit));
    }

    @Override
    public ServerScheduler schedule(Runnable task, long delay, TimeUnit unit) {
        return new BungeeSchedulerWrapper(PROXY_SERVER.getScheduler().schedule(BungeeAuthPluginBootstrap.getInstance(), task, delay, unit));
    }

    @Override
    public void runAsync(Runnable task) {
        EXECUTOR_SERVICE.execute(task);
    }

    @Override
    public String colorize(String text) {
        return BungeeComponent.colorText(text);
    }
}
