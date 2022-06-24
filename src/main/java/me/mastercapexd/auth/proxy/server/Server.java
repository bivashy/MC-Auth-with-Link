package me.mastercapexd.auth.proxy.server;

import java.util.List;

import me.mastercapexd.auth.function.Castable;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;

public interface Server extends Castable<Server> {
    String getServerName();

    void sendPlayer(ProxyPlayer... players);

    List<ProxyPlayer> getPlayers();

    int getPlayersCount();

    /**
     * Validate if server exists, or not. By default just check if original server
     * value is null.
     *
     * @return is valid server.
     */
    boolean isExists();
}
