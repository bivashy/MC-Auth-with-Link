package me.mastercapexd.auth.proxy.commands.parameters;

import java.util.Optional;
import java.util.UUID;

import me.mastercapexd.auth.proxy.message.ProxyComponent;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.proxy.server.Server;

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
    public void disconnect(ProxyComponent component) {
        player.disconnect(component);
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    @Override
    public void sendMessage(ProxyComponent component) {
        player.sendMessage(component);
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
    public Optional<Server> getCurrentServer() {
        return player.getCurrentServer();
    }

    @Override
    public <T> T getRealPlayer() {
        return player.getRealPlayer();
    }

}
