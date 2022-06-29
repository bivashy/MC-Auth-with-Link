package me.mastercapexd.auth.proxy;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import me.mastercapexd.auth.function.Castable;
import me.mastercapexd.auth.proxy.api.bossbar.ProxyBossbar;
import me.mastercapexd.auth.proxy.api.title.ProxyTitle;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.proxy.server.Server;

public interface ProxyCore extends Castable<ProxyCore> {
    <E> void callEvent(E event);

    List<ProxyPlayer> getPlayers();

    Optional<ProxyPlayer> getPlayer(UUID uniqueId);

    Optional<ProxyPlayer> getPlayer(String name);

    Optional<ProxyPlayer> wrapPlayer(Object player);

    Logger getLogger();

    ProxyTitle createTitle(String title);

    ProxyBossbar createBossbar(String title);

    ProxyBossbar createBossbar(ProxyComponent barText);

    ProxyComponent componentPlain(String plain);

    ProxyComponent componentJson(String json);

    ProxyComponent componentLegacy(String legacy);

    Optional<Server> serverFromName(String serverName);

    void registerListener(ProxyPlugin plugin, Object listener);

    void schedule(ProxyPlugin plugin, Runnable task, long delay, long period, TimeUnit unit);

    void schedule(ProxyPlugin instance, Runnable task, long joinDelay, TimeUnit milliseconds);

    void runAsync(Runnable task);

    String colorize(String text);
}
