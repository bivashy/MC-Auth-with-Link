package me.mastercapexd.auth.velocity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import me.mastercapexd.auth.proxy.ProxyCore;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.api.bossbar.ProxyBossbar;
import me.mastercapexd.auth.proxy.api.title.ProxyTitle;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.proxy.server.Server;
import me.mastercapexd.auth.velocity.api.bossbar.VelocityProxyBossbar;
import me.mastercapexd.auth.velocity.component.VelocityComponent;
import me.mastercapexd.auth.velocity.player.VelocityProxyPlayer;
import me.mastercapexd.auth.velocity.server.VelocityServer;
import me.mastercapexd.auth.velocity.api.title.VelocityProxyTitle;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class VelocityProxyCore implements ProxyCore {
    private final ProxyServer server;

    public VelocityProxyCore(ProxyServer server) {
        this.server = server;
    }


    @Override
    public <E> void callEvent(E event) {
        server.getEventManager().fireAndForget(event);
    }

    @Override
    public List<ProxyPlayer> getPlayers() {
        return server.getAllPlayers().stream().map(VelocityProxyPlayer::new).collect(Collectors.toList());
    }

    @Override
    public Optional<ProxyPlayer> getPlayer(UUID uniqueId) {
        return server.getPlayer(uniqueId).map(VelocityProxyPlayer::new);
    }

    @Override
    public Optional<ProxyPlayer> getPlayer(String name) {
        return server.getPlayer(name).map(VelocityProxyPlayer::new);
    }

    @Override
    public Optional<ProxyPlayer> wrapPlayer(Object player) {
        if (player == null)
            return Optional.empty();
        if (player instanceof ProxyPlayer)
            return Optional.of((ProxyPlayer) player);
        if (player instanceof Player)
            return Optional.of(new VelocityProxyPlayer((Player) player));
        return Optional.empty();
    }

    @Override
    public Logger getLogger() {
        return null;
    }

    @Override
    public ProxyTitle createTitle(String title) {
        return new VelocityProxyTitle(title);
    }

    @Override
    public ProxyBossbar createBossbar(String title) {
        return new VelocityProxyBossbar(title);
    }

    @Override
    public ProxyBossbar createBossbar(ProxyComponent component) {
        return new VelocityProxyBossbar(component.as(VelocityComponent.class).component());
    }

    @Override
    public ProxyComponent componentPlain(String plain) {
        return new VelocityComponent(PlainTextComponentSerializer.plainText().deserialize(plain));
    }

    @Override
    public ProxyComponent componentJson(String json) {
        return new VelocityComponent(GsonComponentSerializer.gson().deserialize(json));
    }

    @Override
    public ProxyComponent componentLegacy(String legacy) {
        return new VelocityComponent(LegacyComponentSerializer.legacyAmpersand().deserialize(legacy));
    }

    @Override
    public Optional<Server> serverFromName(String serverName) {
        Optional<RegisteredServer> serverOptional = server.getServer(serverName);
        return serverOptional.map(VelocityServer::new);
    }

    @Override
    public void registerListener(ProxyPlugin plugin, Object listener) {
        server.getEventManager().register(plugin, listener);
    }

    @Override
    public void schedule(ProxyPlugin plugin, Runnable task, long delay, long period, TimeUnit unit) {
        server.getScheduler().buildTask(plugin, task).delay(delay, unit).repeat(period, unit).schedule();
    }

    @Override
    public void schedule(ProxyPlugin plugin, Runnable task, long delay, TimeUnit unit) {
        server.getScheduler().buildTask(plugin, task).delay(delay, unit).schedule();
    }

    @Override
    public void runAsync(Runnable task) {
        server.getScheduler().buildTask(AuthPlugin.getInstance(), task).schedule();
    }

    @Override
    public String colorize(String text) {
        return text;
    }
}
