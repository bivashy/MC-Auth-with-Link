package me.mastercapexd.auth.bungee.server;

import java.util.List;
import java.util.stream.Collectors;

import com.bivashy.auth.api.server.player.ServerPlayer;
import com.bivashy.auth.api.server.proxy.ProxyServer;

import me.mastercapexd.auth.bungee.player.BungeeServerPlayer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeServer implements ProxyServer {
    private final ServerInfo bungeeServerInfo;

    public BungeeServer(ServerInfo bungeeServerInfo) {
        this.bungeeServerInfo = bungeeServerInfo;
    }

    @Override
    public String getServerName() {
        return bungeeServerInfo.getName();
    }

    @Override
    public void sendPlayer(ServerPlayer... players) {
        for (ServerPlayer player : players) {
            ProxiedPlayer bungeePlayer = player.getRealPlayer();
            if (bungeeServerInfo.getName().equals(player.getCurrentServer().map(ProxyServer::getServerName).orElse(null)))
                continue;
            bungeePlayer.connect(bungeeServerInfo, (result, exception) -> {
            });
        }
    }

    @Override
    public List<ServerPlayer> getPlayers() {
        return bungeeServerInfo.getPlayers().stream().map(BungeeServerPlayer::new).collect(Collectors.toList());
    }

    @Override
    public int getPlayersCount() {
        return bungeeServerInfo.getPlayers().size();
    }

    @Override
    public boolean isExists() {
        return bungeeServerInfo != null;
    }

    public ServerInfo getServerInfo() {
        return bungeeServerInfo;
    }
}
