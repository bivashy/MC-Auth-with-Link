package me.mastercapexd.auth.velocity.server;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.server.player.ServerPlayer;
import com.bivashy.auth.api.server.proxy.ProxyServer;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;

import me.mastercapexd.auth.velocity.player.VelocityServerPlayer;

public class VelocityProxyServer implements ProxyServer {
    private final RegisteredServer registeredServer;

    public VelocityProxyServer(RegisteredServer registeredServer) {
        this.registeredServer = registeredServer;
    }

    @Override
    public String getServerName() {
        return registeredServer.getServerInfo().getName();
    }

    @Override
    public void sendPlayer(ServerPlayer... players) {
        for (ServerPlayer player : players) {
            Player proxyPlayer = player.as(VelocityServerPlayer.class).getPlayer();
            if (registeredServer.getServerInfo()
                    .getName()
                    .equals(proxyPlayer.getCurrentServer().map(ServerConnection::getServerInfo).map(ServerInfo::getName).orElse(null)))
                continue;
            proxyPlayer.createConnectionRequest(registeredServer).connect();
        }
    }

    @Override
    public List<ServerPlayer> getPlayers() {
        return registeredServer.getPlayersConnected()
                .stream()
                .map(AuthPlugin.instance().getCore()::wrapPlayer)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public int getPlayersCount() {
        return registeredServer.getPlayersConnected().size();
    }

    @Override
    public boolean isExists() {
        return registeredServer != null;
    }

    public RegisteredServer getServer() {
        return registeredServer;
    }
}
