package me.mastercapexd.auth.proxy.player;

import java.util.UUID;

import me.mastercapexd.auth.function.Castable;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import me.mastercapexd.auth.proxy.server.Server;

public interface ProxyPlayer extends Castable<ProxyPlayer> {
    void disconnect(String reason);

    void disconnect(ProxyComponent component);

    void sendMessage(String message);

    void sendMessage(ProxyComponent component);

    String getNickname();

    UUID getUniqueId();

    String getPlayerIp();

    <T> T getRealPlayer();

    default void sendTo(Server server) {
        server.sendPlayer(this);
    }
}
