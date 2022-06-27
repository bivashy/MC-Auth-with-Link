package me.mastercapexd.auth.proxy.commands.parameters;

import java.util.UUID;

import me.mastercapexd.auth.proxy.player.ProxyPlayer;

public class ArgumentProxyPlayer implements ProxyPlayer {
    private final ProxyPlayer player;

    public ArgumentProxyPlayer(ProxyPlayer player) {
        this.player = player;
    }

    @Override
    public void disconnect(String reason) {
        player.disconnect(reason);
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    @Override
    public String getNickname() {
        return player.getNickname();
    }

    @Override
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    @Override
    public String getPlayerIp() {
        return player.getPlayerIp();
    }

    @Override
    public <T> T getRealPlayer() {
        return player.getRealPlayer();
    }

}
