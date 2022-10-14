package me.mastercapexd.auth.velocity.server;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.proxy.server.Server;
import me.mastercapexd.auth.velocity.player.VelocityProxyPlayer;

public class VelocityServer implements Server {
    private final RegisteredServer registeredServer;

    public VelocityServer(RegisteredServer registeredServer) {
        this.registeredServer = registeredServer;
    }

    @Override
    public String getServerName() {
        return registeredServer.getServerInfo().getName();
    }

    @Override
    public void sendPlayer(ProxyPlayer... players) {
        for (ProxyPlayer player : players) {
            Player proxyPlayer = player.as(VelocityProxyPlayer.class).getPlayer();
            if (proxyPlayer.getCurrentServer().isPresent() && proxyPlayer.getCurrentServer().get().getServerInfo().equals(registeredServer.getServerInfo()))
                continue;
            proxyPlayer.createConnectionRequest(registeredServer).connect();
        }
    }

    @Override
    public List<ProxyPlayer> getPlayers() {
        return registeredServer.getPlayersConnected().stream().map(ProxyPlugin.instance().getCore()::wrapPlayer).map(Optional::get)
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
