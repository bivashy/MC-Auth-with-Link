package com.bivashy.auth.api.server.proxy;

import java.util.List;

import com.bivashy.auth.api.server.player.ServerPlayer;
import com.bivashy.auth.api.util.Castable;

public interface ProxyServer extends Castable<ProxyServer> {
    String getServerName();

    void sendPlayer(ServerPlayer... players);

    List<ServerPlayer> getPlayers();

    int getPlayersCount();

    /**
     * Validate if server exists, or not. By default just check if original server
     * value is null.
     *
     * @return is valid server.
     */
    boolean isExists();
}
