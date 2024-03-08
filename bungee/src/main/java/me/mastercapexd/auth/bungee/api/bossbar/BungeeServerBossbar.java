package me.mastercapexd.auth.bungee.api.bossbar;

import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.auth.api.server.player.ServerPlayer;

import me.mastercapexd.auth.bungee.BungeeAuthPluginBootstrap;
import me.mastercapexd.auth.bungee.player.BungeeServerPlayer;
import me.mastercapexd.auth.server.adventure.AdventureServerBossbar;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;

public class BungeeServerBossbar extends AdventureServerBossbar {

    private final BungeeAudiences audiences = BungeeAuthPluginBootstrap.getInstance().getBungeeAudiences();

    public BungeeServerBossbar(ServerComponent component) {
        super(component);
    }

    @Override
    public void showBossBar(ServerPlayer player, BossBar bossBar) {
        audiences.player(player.as(BungeeServerPlayer.class).getBungeePlayer()).showBossBar(bossBar);
    }

    @Override
    public void hideBossBar(ServerPlayer player, BossBar bossBar) {
        audiences.player(player.as(BungeeServerPlayer.class).getBungeePlayer()).hideBossBar(bossBar);
    }

}

