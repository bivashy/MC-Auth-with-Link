package me.mastercapexd.auth.bungee.player;

import java.net.InetSocketAddress;
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

    @SuppressWarnings("deprecation")
    @Override
    public InetSocketAddress getRemoteAddress() {
        return player.getAddress();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getRealPlayer() {
        return (T) getBungeePlayer();
    }

    public ProxiedPlayer getBungeePlayer() {
        return player;
    }

    public static class BungeeProxyPlayerFactory {
        public static BungeeProxyPlayer wrapPlayer(ProxiedPlayer player) {
            return new BungeeProxyPlayer(player);
        }
    }

}
