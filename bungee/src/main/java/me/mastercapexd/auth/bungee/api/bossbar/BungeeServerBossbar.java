package me.mastercapexd.auth.bungee.api.bossbar;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import com.bivashy.auth.api.server.bossbar.ServerBossbar;
import com.bivashy.auth.api.server.player.ServerPlayer;
import com.google.common.collect.Sets;

import me.mastercapexd.auth.bungee.player.BungeeServerPlayer;
import net.md_5.bungee.protocol.packet.BossBar;

public class BungeeServerBossbar extends ServerBossbar {
    private final Set<ServerPlayer> players = Sets.newHashSet();
    private final UUID uuid = UUID.randomUUID();

    public BungeeServerBossbar(String title) {
        this.title(title);
    }

    @Override
    public ServerBossbar removeAll() {
        this.players.forEach(this::remove);
        this.players.clear();
        return this;
    }

    @Override
    public ServerBossbar send(ServerPlayer... viewers) {
        BossBar bossBar = getAddPacket();
        for (ServerPlayer player : viewers) {
            player.as(BungeeServerPlayer.class).getBungeePlayer().unsafe().sendPacket(bossBar);
            players.add(player);
        }
        return this;
    }

    @Override
    public ServerBossbar remove(ServerPlayer... viewers) {
        BossBar bossBar = getRemovePacket();
        for (ServerPlayer player : viewers) {
            player.as(BungeeServerPlayer.class).getBungeePlayer().unsafe().sendPacket(bossBar);
            players.remove(player);
        }
        return this;
    }

    @Override
    public ServerBossbar update() {
        BossBar bossBar = getAddPacket();
        for (ServerPlayer player : players)
            player.as(BungeeServerPlayer.class).getBungeePlayer().unsafe().sendPacket(bossBar);
        return this;
    }

    @Override
    public Collection<ServerPlayer> players() {
        return Collections.unmodifiableCollection(players);
    }

    private BossBar getAddPacket() {
        BossBar packet = new BossBar(uuid, 0);

        packet.setTitle(title); // We send title in json
        packet.setColor(this.color.ordinal());
        packet.setDivision(this.segmentStyle.ordinal());
        packet.setHealth(this.progress);
        return packet;
    }

    private BossBar getRemovePacket() {
        return new BossBar(uuid, 1);
    }
}
