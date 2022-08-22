package me.mastercapexd.auth.proxy.server.limbo;

import java.util.Collections;
import java.util.List;

import com.ubivashka.limbo.ProxyLimbo;
import com.ubivashka.limbo.player.LimboPlayer;
import com.ubivashka.limbo.server.LimboServer;

import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.proxy.server.Server;

public class ProxyLimboServerWrapper implements LimboServerWrapper {
    private final LimboServer limboServer;

    public ProxyLimboServerWrapper(LimboServer limboServer) {
        this.limboServer = limboServer;
    }


    @Override
    public String getServerName() {
        return limboServer.getName();
    }

    @Override
    public void sendPlayer(ProxyPlayer... players) {
        for (ProxyPlayer player : players) {
            LimboPlayer limboPlayer = ProxyLimbo.instance().findLimboPlayer(player.getUniqueId());
            limboServer.connect(limboPlayer);
        }
    }

    @Override
    public List<ProxyPlayer> getPlayers() {
        return Collections.emptyList();
    }

    @Override
    public int getPlayersCount() {
        return 0;
    }

    @Override
    public boolean isExists() {
        return limboServer != null;
    }
}
