package me.mastercapexd.auth.bungee.player;

import java.util.UUID;

import me.mastercapexd.auth.bungee.BungeeProxyCore;
import me.mastercapexd.auth.bungee.message.BungeeComponent;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeProxyPlayer implements ProxyPlayer {
    private final ProxiedPlayer player;

    public BungeeProxyPlayer(ProxiedPlayer player) {
        this.player = player;
    }

    @Override
    public void disconnect(String reason) {
        disconnect(BungeeProxyCore.INSTANCE.componentLegacy(reason));
    }

    @Override
    public void disconnect(ProxyComponent component) {
        player.disconnect(component.as(BungeeComponent.class).components());
    }

    @Override
    public void sendMessage(String message) {
        if (message.isEmpty())
            return;
        player.sendMessage(message);
    }

    @Override
    public void sendMessage(ProxyComponent proxyComponent) {
        player.sendMessage(proxyComponent.as(BungeeComponent.class).components());
    }

    @Override
    public String getNickname() {
        return player.getName();
    }

    @Override
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    @Override
    public String getPlayerIp() {
        return player.getAddress().getHostName();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getRealPlayer() {
        return (T) getBungeePlayer();
    }

    public ProxiedPlayer getBungeePlayer() {
        return player;
    }
}
