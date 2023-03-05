package me.mastercapexd.auth.velocity.hooks.limbo;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.bivashy.auth.api.server.player.ServerPlayer;
import com.bivashy.auth.api.server.proxy.limbo.LimboServerWrapper;
import com.velocitypowered.api.proxy.Player;

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
    public void sendPlayer(ServerPlayer... players) {
        for (ServerPlayer player : players) {
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
    public List<ServerPlayer> getPlayers() {
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
