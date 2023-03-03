package com.bivashy.auth.api.server;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.server.bossbar.ServerBossbar;
import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.auth.api.server.player.ServerPlayer;
import com.bivashy.auth.api.server.proxy.ProxyServer;
import com.bivashy.auth.api.server.scheduler.ServerScheduler;
import com.bivashy.auth.api.server.title.ServerTitle;
import com.bivashy.auth.api.util.Castable;

import net.kyori.adventure.audience.Audience;

public interface ServerCore extends Castable<ServerCore> {
    <E> void callEvent(E event);

    List<ServerPlayer> getPlayers();

    Optional<ServerPlayer> getPlayer(UUID uniqueId);

    Optional<ServerPlayer> getPlayer(String name);

    Optional<ServerPlayer> wrapPlayer(Object player);

    Logger getLogger();

    ServerTitle createTitle(ServerComponent title);

    ServerBossbar createBossbar(ServerComponent barText);

    ServerComponent componentPlain(String plain);

    ServerComponent componentJson(String json);

    ServerComponent componentLegacy(String legacy);

    Optional<ProxyServer> serverFromName(String serverName);

    void registerListener(AuthPlugin plugin, Object listener);

    ServerScheduler schedule(AuthPlugin plugin, Runnable task, long delay, long period, TimeUnit unit);

    ServerScheduler schedule(AuthPlugin instance, Runnable task, long delay, TimeUnit milliseconds);

    void runAsync(Runnable task);

    default Audience getAudience(ServerPlayer player) {
        return AuthPlugin.instance().getAudienceProvider().player(player.getUniqueId());
    }

    String colorize(String text);
}
