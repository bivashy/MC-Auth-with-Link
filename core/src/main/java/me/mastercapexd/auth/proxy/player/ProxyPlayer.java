package me.mastercapexd.auth.proxy.player;

import java.util.Optional;
import java.util.UUID;

import me.mastercapexd.auth.function.Castable;
import me.mastercapexd.auth.model.PlayerIdSupplier;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.adventure.AdventureProxyComponent;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import me.mastercapexd.auth.proxy.server.Server;

public interface ProxyPlayer extends Castable<ProxyPlayer>, PlayerIdSupplier {
    @Override
    default String getPlayerId() {
        return ProxyPlugin.instance().getConfig().getActiveIdentifierType().getId(this);
    }

    default void disconnect(String reason) {
        disconnect(new AdventureProxyComponent(ProxyPlugin.instance().getConfig().getProxyMessages().getDeserializer().deserialize(reason)));
    }

    default void sendMessage(String message) {
        if (message.isEmpty())
            return;
        new AdventureProxyComponent(ProxyPlugin.instance().getConfig().getProxyMessages().getDeserializer().deserialize(message)).send(this);
    }

    void disconnect(ProxyComponent component);

    void sendMessage(ProxyComponent component);

    String getNickname();

    UUID getUniqueId();

    String getPlayerIp();

    Optional<Server> getCurrentServer();

    <T> T getRealPlayer();

    default void sendTo(Server server) {
        server.sendPlayer(this);
    }
}
