package me.mastercapexd.auth.velocity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.server.ServerCore;
import com.bivashy.auth.api.server.bossbar.ServerBossbar;
import com.bivashy.auth.api.server.message.AdventureServerComponent;
import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.auth.api.server.player.ServerPlayer;
import com.bivashy.auth.api.server.proxy.limbo.LimboServerWrapper;
import com.bivashy.auth.api.server.scheduler.ServerScheduler;
import com.bivashy.auth.api.server.title.ServerTitle;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import me.mastercapexd.auth.hooks.limbo.LimboHook;
import me.mastercapexd.auth.velocity.api.bossbar.VelocityServerBossbar;
import me.mastercapexd.auth.velocity.api.title.VelocityServerTitle;
import me.mastercapexd.auth.velocity.component.VelocityComponent;
import me.mastercapexd.auth.velocity.player.VelocityServerPlayer;
import me.mastercapexd.auth.velocity.scheduler.VelocitySchedulerWrapper;
import me.mastercapexd.auth.velocity.server.VelocityProxyServer;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class VelocityProxyCore implements ServerCore {
    private final ProxyServer server;

    public VelocityProxyCore(ProxyServer server) {
        this.server = server;
    }

    @Override
    public <E> void callEvent(E event) {
        server.getEventManager().fireAndForget(event);
    }

    @Override
    public List<ServerPlayer> getPlayers() {
        return server.getAllPlayers().stream().map(VelocityServerPlayer::new).collect(Collectors.toList());
    }

    @Override
    public Optional<ServerPlayer> getPlayer(UUID uniqueId) {
        return server.getPlayer(uniqueId).map(VelocityServerPlayer::new);
    }

    @Override
    public Optional<ServerPlayer> getPlayer(String name) {
        return server.getPlayer(name).map(VelocityServerPlayer::new);
    }

    @Override
    public Optional<ServerPlayer> wrapPlayer(Object player) {
        if (player == null)
            return Optional.empty();
        if (player instanceof ServerPlayer)
            return Optional.of((ServerPlayer) player);
        if (player instanceof Player)
            return Optional.of(new VelocityServerPlayer((Player) player));
        return Optional.empty();
    }

    @Override
    public Logger getLogger() {
        return null;
    }

    @Override
    public ServerTitle createTitle(ServerComponent title) {
        return new VelocityServerTitle(title);
    }

    @Override
    public ServerBossbar createBossbar(ServerComponent component) {
        return new VelocityServerBossbar(component.as(AdventureServerComponent.class).component());
    }

    @Override
    public ServerComponent componentPlain(String plain) {
        return new VelocityComponent(PlainTextComponentSerializer.plainText().deserialize(plain));
    }

    @Override
    public ServerComponent componentJson(String json) {
        return new VelocityComponent(GsonComponentSerializer.gson().deserialize(json));
    }

    @Override
    public ServerComponent componentLegacy(String legacy) {
        return new VelocityComponent(LegacyComponentSerializer.legacyAmpersand().deserialize(legacy));
    }

    @Override
    public Optional<com.bivashy.auth.api.server.proxy.ProxyServer> serverFromName(String serverName) {
        Optional<RegisteredServer> serverOptional = server.getServer(serverName);
        LimboHook limboHook = AuthPlugin.instance().getHook(LimboHook.class);
        if (!serverOptional.isPresent() && limboHook != null) {
            if (!limboHook.isLimbo(serverName))
                return Optional.empty();
            LimboServerWrapper server = limboHook.createLimboWrapper(serverName);
            if (!server.isExists())
                return Optional.empty();
            return Optional.of(server);
        }

        return serverOptional.map(VelocityProxyServer::new);
    }

    @Override
    public void registerListener(AuthPlugin plugin, Object listener) {
        server.getEventManager().register(plugin, listener);
    }

    @Override
    public ServerScheduler schedule(Runnable task, long delay, long period, TimeUnit unit) {
        return new VelocitySchedulerWrapper(
                server.getScheduler().buildTask(VelocityAuthPluginBootstrap.getInstance(), task).delay(delay, unit).repeat(period, unit).schedule());
    }

    @Override
    public ServerScheduler schedule(Runnable task, long delay, TimeUnit unit) {
        return new VelocitySchedulerWrapper(server.getScheduler().buildTask(VelocityAuthPluginBootstrap.getInstance(), task).delay(delay, unit).schedule());
    }

    @Override
    public void runAsync(Runnable task) {
        server.getScheduler().buildTask(VelocityAuthPluginBootstrap.getInstance(), task).schedule();
    }

    @Override
    public String colorize(String text) {
        return text;
    }
}
