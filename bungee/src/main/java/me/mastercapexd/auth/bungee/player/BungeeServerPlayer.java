package me.mastercapexd.auth.bungee.player;

import java.util.Optional;
import java.util.UUID;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.server.message.SelfHandledServerComponent;
import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.auth.api.server.player.ServerPlayer;
import com.bivashy.auth.api.server.proxy.ProxyServer;

import me.mastercapexd.auth.bungee.message.BungeeComponent;
import me.mastercapexd.auth.bungee.server.BungeeServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeServerPlayer implements ServerPlayer {
    private final ProxiedPlayer player;

    public BungeeServerPlayer(ProxiedPlayer player) {
        this.player = player;
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public void disconnect(ServerComponent component) {
        if (component.safeAs(SelfHandledServerComponent.class).isPresent()) {
            disconnect(AuthPlugin.instance().getCore().componentJson(component.jsonText()));
            return;
        }
        player.disconnect(component.as(BungeeComponent.class).components());
    }

    @Override
    public void sendMessage(ServerComponent component) {
        if (component.safeAs(SelfHandledServerComponent.class).isPresent()) {
            component.as(SelfHandledServerComponent.class).send(this);
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
        return player.getAddress().getAddress().getHostAddress();
    }

    @Override
    public Optional<ProxyServer> getCurrentServer() {
        if (player.getServer() == null)
            return Optional.empty();
        return Optional.of(new BungeeServer(player.getServer().getInfo()));
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
