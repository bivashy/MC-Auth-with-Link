package me.mastercapexd.auth.velocity.hooks.limbo;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.velocitypowered.api.proxy.Player;

import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.proxy.server.limbo.LimboServerWrapper;
import net.elytrium.limboapi.api.Limbo;
import net.elytrium.limboapi.api.LimboSessionHandler;

public class LimboAPIServer implements LimboServerWrapper {
    private final Set<UUID> onlinePlayers = new HashSet<>();
    private final String name;
    private final Limbo limbo;

    public LimboAPIServer(String name, Limbo limbo) {
        this.name = name;
        this.limbo = limbo;
    }

    @Override
    public String getServerName() {
        return name;
    }

    @Override
    public void sendPlayer(ProxyPlayer... players) {
        for (ProxyPlayer player : players) {
            if (onlinePlayers.contains(player.getUniqueId()))
                continue;
            onlinePlayers.add(player.getUniqueId());
            Player velocityPlayer = player.getRealPlayer();
            limbo.spawnPlayer(velocityPlayer, new LimboSessionHandler() {
                @Override
                public void onDisconnect() {
                    onlinePlayers.remove(player.getUniqueId());
                }
            });
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
        return limbo != null;
    }
}
