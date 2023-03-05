package me.mastercapexd.auth.server.commands.parameters;

import java.util.Optional;
import java.util.UUID;

import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.auth.api.server.player.ServerPlayer;
import com.bivashy.auth.api.server.proxy.ProxyServer;

public class ArgumentProxyPlayer implements ServerPlayer {
    private final ServerPlayer player;

    public ArgumentProxyPlayer(ServerPlayer player) {
        this.player = player;
    }

    @Override
    public void disconnect(String reason) {
        player.disconnect(reason);
    }

    @Override
    public void disconnect(ServerComponent component) {
        player.disconnect(component);
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    @Override
    public void sendMessage(ServerComponent component) {
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
    public Optional<ProxyServer> getCurrentServer() {
        return player.getCurrentServer();
    }

    @Override
    public <T> T getRealPlayer() {
        return player.getRealPlayer();
    }
}
