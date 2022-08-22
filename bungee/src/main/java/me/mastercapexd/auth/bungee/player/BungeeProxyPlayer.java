package me.mastercapexd.auth.bungee.player;

import java.util.UUID;

import me.mastercapexd.auth.bungee.message.BungeeComponent;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import me.mastercapexd.auth.proxy.message.SelfHandledProxyComponent;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeProxyPlayer implements ProxyPlayer {
    private final ProxiedPlayer player;

    public BungeeProxyPlayer(ProxiedPlayer player) {
        this.player = player;
    }

    @Override
    public void disconnect(ProxyComponent component) {
        if (component.safeAs(SelfHandledProxyComponent.class).isPresent()) {
            disconnect(ProxyPlugin.instance().getCore().componentJson(component.jsonText()));
            return;
        }
        player.disconnect(component.as(BungeeComponent.class).components());
    }

    @Override
    public void sendMessage(ProxyComponent component) {
        if (component.safeAs(SelfHandledProxyComponent.class).isPresent()) {
            component.as(SelfHandledProxyComponent.class).send(this);
            return;
        }
        player.sendMessage(component.as(BungeeComponent.class).components());
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
