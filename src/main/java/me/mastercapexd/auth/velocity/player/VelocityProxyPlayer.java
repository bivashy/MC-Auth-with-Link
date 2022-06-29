package me.mastercapexd.auth.velocity.player;

import java.util.UUID;

import com.velocitypowered.api.proxy.Player;

import me.mastercapexd.auth.proxy.message.ProxyComponent;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.velocity.component.VelocityComponent;

public class VelocityProxyPlayer implements ProxyPlayer {
    private final Player player;

    public VelocityProxyPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void disconnect(String reason) {
        player.disconnect(VelocityComponent.LEGACY_COMPONENT_SERIALIZER.deserialize(reason));
    }

    @Override
    public void disconnect(ProxyComponent component) {
        player.disconnect(component.as(VelocityComponent.class).component());
    }

    @Override
    public void sendMessage(String message) {
        if (message.isEmpty())
            return;
        player.sendMessage(VelocityComponent.LEGACY_COMPONENT_SERIALIZER.deserialize(message));
    }

    @Override
    public void sendMessage(ProxyComponent component) {
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
    public <T> T getRealPlayer() {
        return (T) getPlayer();
    }

    public Player getPlayer() {
        return player;
    }
}
