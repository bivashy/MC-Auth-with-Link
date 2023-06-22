package me.mastercapexd.auth.velocity.player;

import java.util.Optional;
import java.util.UUID;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.server.message.AdventureServerComponent;
import com.bivashy.auth.api.server.message.SelfHandledServerComponent;
import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.auth.api.server.player.ServerPlayer;
import com.bivashy.auth.api.server.proxy.ProxyServer;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;

import me.mastercapexd.auth.velocity.server.VelocityProxyServer;
import net.kyori.adventure.text.Component;

public class VelocityServerPlayer implements ServerPlayer {
    private final Player player;

    public VelocityServerPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void disconnect(ServerComponent component) {
        if (component.safeAs(SelfHandledServerComponent.class).isPresent()) {
            disconnect(AuthPlugin.instance().getCore().componentJson(component.jsonText()));
            return;
        }
        Optional<Component> optionalComponent = component.safeAs(AdventureServerComponent.class).map(AdventureServerComponent::component);
        if (!optionalComponent.isPresent())
            throw new UnsupportedOperationException("Cannot retrieve kyori Component from: " + component.getClass().getSimpleName() + ", " + component);
        player.disconnect(optionalComponent.get());
    }

    @Override
    public void sendMessage(ServerComponent component) {
        if (component.safeAs(SelfHandledServerComponent.class).isPresent()) {
            component.as(SelfHandledServerComponent.class).send(this);
            return;
        }
        Optional<Component> optionalComponent = component.safeAs(AdventureServerComponent.class).map(AdventureServerComponent::component);
        if (!optionalComponent.isPresent())
            throw new UnsupportedOperationException("Cannot retrieve kyori Component from: " + component.getClass().getSimpleName() + ", " + component);
        player.sendMessage(optionalComponent.get());
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
    public Optional<ProxyServer> getCurrentServer() {
        return player.getCurrentServer().map(ServerConnection::getServer).map(VelocityProxyServer::new);
    }

    @Override
    public <T> T getRealPlayer() {
        return (T) getPlayer();
    }

    public Player getPlayer() {
        return player;
    }
}
