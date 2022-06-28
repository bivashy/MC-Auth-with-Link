package me.mastercapexd.auth.bungee.player;

import java.util.UUID;

import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeProxyPlayer implements ProxyPlayer {
    private final ProxiedPlayer player;

    public BungeeProxyPlayer(ProxiedPlayer player) {
        this.player = player;
    }

    @Override
    public void disconnect(String reason) {
        player.disconnect(TextComponent.fromLegacyText(reason));
    }

    @Override
    public void sendMessage(String message) {
        if (message.isEmpty())
            return;
        player.sendMessage(TextComponent.fromLegacyText(message));
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
        return player.getSocketAddress().toString();
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
