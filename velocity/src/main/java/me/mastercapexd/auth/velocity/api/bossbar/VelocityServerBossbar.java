package me.mastercapexd.auth.velocity.api.bossbar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.server.bossbar.ServerBossbar;
import com.bivashy.auth.api.server.message.AdventureServerComponent;
import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.auth.api.server.player.ServerPlayer;

import me.mastercapexd.auth.velocity.player.VelocityServerPlayer;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class VelocityServerBossbar extends ServerBossbar {
    private final List<ServerPlayer> bossBarPlayers = new ArrayList<>();
    private final BossBar bossBar;

    public VelocityServerBossbar(String title) {
        title(title);
        BossBar.Color bossBarColor = BossBar.Color.values()[color.ordinal()];
        BossBar.Overlay bossBarOverlay = BossBar.Overlay.values()[segmentStyle.ordinal()];
        bossBar = BossBar.bossBar(LegacyComponentSerializer.legacyAmpersand().deserialize(title), progress, bossBarColor, bossBarOverlay).progress(progress);
    }

    public VelocityServerBossbar(Component component) {
        this(LegacyComponentSerializer.legacySection().serialize(component));
    }

    @Override
    public ServerBossbar send(ServerPlayer... viewers) {
        for (ServerPlayer player : viewers) {
            player.as(VelocityServerPlayer.class).getPlayer().showBossBar(bossBar);
            bossBarPlayers.add(player);
        }
        return this;
    }

    @Override
    public ServerBossbar remove(ServerPlayer... viewers) {
        for (ServerPlayer player : viewers) {
            player.as(VelocityServerPlayer.class).getPlayer().hideBossBar(bossBar);
            bossBarPlayers.remove(player);
        }
        return this;
    }

    @Override
    public ServerBossbar update() {
        ServerComponent bossbarTitleComponent = AuthPlugin.instance().getConfig().getServerMessages().getDeserializer().deserialize(title);
        BossBar.Color bossBarColor = BossBar.Color.values()[color.ordinal()];
        BossBar.Overlay bossBarOverlay = BossBar.Overlay.values()[segmentStyle.ordinal()];

        bossbarTitleComponent.safeAs(AdventureServerComponent.class).map(AdventureServerComponent::component).ifPresent(bossBar::name);
        bossBar.color(bossBarColor).overlay(bossBarOverlay).progress(progress);
        return this;
    }

    @Override
    public ServerBossbar removeAll() {
        remove(bossBarPlayers.toArray(new ServerPlayer[0]));
        return this;
    }

    @Override
    public Collection<ServerPlayer> players() {
        return Collections.unmodifiableList(bossBarPlayers);
    }
}
