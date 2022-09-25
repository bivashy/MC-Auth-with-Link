package me.mastercapexd.auth.velocity.player;

import java.util.Optional;
import java.util.UUID;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;

import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import me.mastercapexd.auth.proxy.message.SelfHandledProxyComponent;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.proxy.server.Server;
import me.mastercapexd.auth.velocity.component.VelocityComponent;
import me.mastercapexd.auth.velocity.server.VelocityServer;

public class VelocityProxyPlayer implements ProxyPlayer {
    private final Player player;

    public VelocityProxyPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void disconnect(ProxyComponent component) {
        if (component.safeAs(SelfHandledProxyComponent.class).isPresent()) {
            disconnect(ProxyPlugin.instance().getCore().componentJson(component.jsonText()));
            return;
        }
        player.disconnect(component.as(VelocityComponent.class).component());
    }

    @Override
    public void sendMessage(ProxyComponent component) {
        if (component.safeAs(SelfHandledProxyComponent.class).isPresent()) {
            component.as(SelfHandledProxyComponent.class).send(this);
            return;
        }
        player.sendMessage(component.as(VelocityComponent.class).component());
    }

    @Override
    public String getNickname() {
        return player.getUsername();
    }

    @Override
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    @Override
    public String getPlayerIp() {
        return player.getRemoteAddress().getAddress().getHostAddress();
    }

    @Override
    public Optional<Server> getCurrentServer() {
        return player.getCurrentServer().map(ServerConnection::getServer).map(VelocityServer::new);
    }

    @Override
    public <T> T getRealPlayer() {
        return (T) getPlayer();
    }

    public Player getPlayer() {
        return player;
    }
}
