package me.mastercapexd.auth.proxy;

import me.mastercapexd.auth.function.Castable;
import me.mastercapexd.auth.proxy.api.bossbar.ProxyBossbar;
import me.mastercapexd.auth.proxy.api.title.ProxyTitle;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.proxy.server.Server;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public interface ProxyCore extends Castable<ProxyCore> {
    <E> void callEvent(E event);

    Optional<ProxyPlayer> getPlayer(UUID uniqueId);

    Optional<ProxyPlayer> getPlayer(String name);

    Optional<ProxyPlayer> wrapPlayer(Object player);

    Logger getLogger();

    ProxyTitle createTitle(String title);

    ProxyBossbar createBossbar(String title);

    ProxyComponent component(String text);

    Server serverFromName(String serverName);

    void registerListener(ProxyPlugin plugin, Object listener);

    void schedule(ProxyPlugin plugin, Runnable task, long delay, long period, TimeUnit unit);

    void runAsync(Runnable task);
}
