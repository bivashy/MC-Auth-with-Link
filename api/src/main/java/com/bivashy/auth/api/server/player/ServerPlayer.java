package com.bivashy.auth.api.server.player;

import java.util.Optional;
import java.util.UUID;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.model.PlayerIdSupplier;
import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.auth.api.server.proxy.ProxyServer;

public interface ServerPlayer extends PlayerIdSupplier {
    @Override
    default String getPlayerId() {
        return AuthPlugin.instance().getConfig().getActiveIdentifierType().getId(this);
    }

    default void disconnect(String reason) {
        disconnect(AuthPlugin.instance().getConfig().getServerMessages().getDeserializer().deserialize(reason));
    }

    default void sendMessage(String message) {
        if (message.isEmpty())
            return;
        sendMessage(AuthPlugin.instance().getConfig().getServerMessages().getDeserializer().deserialize(message));
    }

    void disconnect(ServerComponent component);

    void sendMessage(ServerComponent component);

    String getNickname();

    UUID getUniqueId();

    String getPlayerIp();

    Optional<ProxyServer> getCurrentServer();

    <T> T getRealPlayer();
}
