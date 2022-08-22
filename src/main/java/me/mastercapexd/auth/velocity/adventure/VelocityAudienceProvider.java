package me.mastercapexd.auth.velocity.adventure;

import java.util.Optional;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.ServerInfo;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.flattener.ComponentFlattener;

public class VelocityAudienceProvider implements AudienceProvider {
    private final ProxyServer server;

    public VelocityAudienceProvider(ProxyServer server) {
        this.server = server;
    }

    @Override
    public @NotNull Audience all() {
        return server.filterAudience((audience) -> true);
    }

    @Override
    public @NotNull Audience console() {
        return server.filterAudience(audience -> audience instanceof ConsoleCommandSource);
    }

    @Override
    public @NotNull Audience players() {
        return server.filterAudience(audience -> audience instanceof Player);
    }

    @Override
    public @NotNull Audience player(@NotNull UUID playerId) {
        return server.filterAudience(audience -> {
            Optional<UUID> uniqueId = audience.get(Identity.UUID);
            return uniqueId.map(uuid -> uuid.equals(playerId)).orElse(false);
        });
    }

    @Override
    public @NotNull Audience permission(@NotNull String permission) {
        return server.filterAudience(audience -> {
            if (!(audience instanceof Player))
                return false;
            Player player = (Player) audience;
            return player.hasPermission(permission);
        });
    }

    @Override
    public @NotNull Audience world(@NotNull Key world) {
        return server.filterAudience(audience -> false);
    }

    @Override
    public @NotNull Audience server(@NotNull String serverName) {
        return server.filterAudience(audience -> {
            if (!(audience instanceof Player))
                return false;
            Player player = (Player) audience;
            return player.getCurrentServer().map(ServerConnection::getServerInfo).map(ServerInfo::getName).map(name -> name.equals(serverName)).orElse(false);
        });
    }

    @Override
    public @NotNull ComponentFlattener flattener() {
        return ComponentFlattener.basic();
    }

    @Override
    public void close() {
    }
}
