package me.mastercapexd.auth.bungee.player;

import java.util.Optional;
import java.util.UUID;

import me.mastercapexd.auth.bungee.message.BungeeComponent;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import me.mastercapexd.auth.proxy.message.SelfHandledProxyComponent;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.proxy.server.Server;
import net.md_5.bungee.api.connection.PendingConnection;

public class BungeeConnectionProxyPlayer implements ProxyPlayer {
    private final PendingConnection pendingConnection;

    public BungeeConnectionProxyPlayer(PendingConnection pendingConnection) {
        this.pendingConnection = pendingConnection;
    }


    @Override
    public void disconnect(ProxyComponent component) {
        if (component.safeAs(SelfHandledProxyComponent.class).isPresent()) {
            disconnect(ProxyPlugin.instance().getCore().componentJson(component.jsonText()));
            return;
        }
        pendingConnection.disconnect(component.as(BungeeComponent.class).components());
    }

    @Override
    public void sendMessage(ProxyComponent component) {
    }

    @Override
    public String getNickname() {
        return pendingConnection.getName();
    }

    @Override
    public UUID getUniqueId() {
        return pendingConnection.getUniqueId();
    }

    @Override
    public String getPlayerIp() {
        return pendingConnection.getAddress().getAddress().getHostAddress();
    }

    @Override
    public Optional<Server> getCurrentServer() {
        return Optional.empty();
    }

    @Override
    public <T> T getRealPlayer() {
        return (T) pendingConnection;
    }
}
