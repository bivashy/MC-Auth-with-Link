package me.mastercapexd.auth.velocity.bossbar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import me.mastercapexd.auth.proxy.api.bossbar.ProxyBossbar;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.velocity.component.VelocityComponent;
import me.mastercapexd.auth.velocity.player.VelocityProxyPlayer;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class VelocityProxyBossbar extends ProxyBossbar {
    private final List<ProxyPlayer> bossBarPlayers = new ArrayList<>();
    private final BossBar bossBar;


    public VelocityProxyBossbar(String title) {
        title(title);
        BossBar.Color bossBarColor = BossBar.Color.values()[color.ordinal()];
        BossBar.Overlay bossBarOverlay = BossBar.Overlay.values()[segmentStyle.ordinal()];
        bossBar = BossBar.bossBar(LegacyComponentSerializer.legacyAmpersand().deserialize(title), progress, bossBarColor, bossBarOverlay).progress(progress);
    }

    public VelocityProxyBossbar(Component component) {
        this(LegacyComponentSerializer.legacySection().serialize(component));
    }

    @Override
    public ProxyBossbar send(ProxyPlayer... viewers) {
        for (ProxyPlayer player : viewers) {
            player.as(VelocityProxyPlayer.class).getPlayer().showBossBar(bossBar);
            bossBarPlayers.add(player);
        }
        return this;
    }

    @Override
    public ProxyBossbar remove(ProxyPlayer... viewers) {
        for (ProxyPlayer player : viewers) {
            player.as(VelocityProxyPlayer.class).getPlayer().hideBossBar(bossBar);
            bossBarPlayers.remove(player);
        }
        return this;
    }

    @Override
    public ProxyBossbar update() {
        Component bossBarTitle = VelocityComponent.LEGACY_COMPONENT_SERIALIZER.deserialize(title);
        BossBar.Color bossBarColor = BossBar.Color.values()[color.ordinal()];
        BossBar.Overlay bossBarOverlay = BossBar.Overlay.values()[segmentStyle.ordinal()];
        bossBar.name(bossBarTitle).color(bossBarColor).overlay(bossBarOverlay).progress(progress);
        return this;
    }

    @Override
    public ProxyBossbar removeAll() {
        remove(bossBarPlayers.toArray(new ProxyPlayer[0]));
        return this;
    }

    @Override
    public Collection<ProxyPlayer> players() {
        return Collections.unmodifiableList(bossBarPlayers);
    }
}
