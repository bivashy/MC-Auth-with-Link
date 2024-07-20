package me.mastercapexd.auth.bungee.player;

import java.util.Optional;
import java.util.UUID;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.server.message.SelfHandledServerComponent;
import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.auth.api.server.player.ServerPlayer;
import com.bivashy.auth.api.server.proxy.ProxyServer;

import me.mastercapexd.auth.bungee.message.BungeeComponent;
import net.md_5.bungee.api.connection.PendingConnection;

public class BungeeConnectionProxyPlayer implements ServerPlayer {
    private final PendingConnection pendingConnection;

    public BungeeConnectionProxyPlayer(PendingConnection pendingConnection) {
        this.pendingConnection = pendingConnection;
    }

    @Override
    public boolean hasPermission(String permission) {
        return false;
    }

    @Override
    public void disconnect(ServerComponent component) {
        if (component.safeAs(SelfHandledServerComponent.class).isPresent()) {
            disconnect(AuthPlugin.instance().getCore().componentJson(component.jsonText()));
            return;
        }
        pendingConnection.disconnect(component.as(BungeeComponent.class).components());
    }

    @Override
    public void sendMessage(ServerComponent component) {
    }

    @Override
    public String getNickname() {
        return pendingConnection.getName();
    }

    @Override
    public UUID getUniqueId() {
        return pendingConnection.getUniqueId();
    }

    @Override
    public String getPlayerIp() {
        return pendingConnection.getAddress().getAddress().getHostAddress();
    }

    @Override
    public Optional<ProxyServer> getCurrentServer() {
        return Optional.empty();
    }

    @Override
    public boolean isOnlineMode() {
        return pendingConnection.isOnlineMode();
    }

    @Override
    public <T> T getRealPlayer() {
        return (T) pendingConnection;
    }
}
