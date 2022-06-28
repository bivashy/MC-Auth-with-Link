package me.mastercapexd.auth.velocity.player;

import java.util.UUID;

import com.velocitypowered.api.proxy.Player;

import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import net.kyori.adventure.text.Component;

public class VelocityProxyPlayer implements ProxyPlayer {
    private final Player player;

    public VelocityProxyPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void disconnect(String reason) {
        player.disconnect(Component.text(reason));
    }

    @Override
    public void sendMessage(String message) {
        if (message.isEmpty())
            return;
        player.sendMessage(Component.text(message));
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
