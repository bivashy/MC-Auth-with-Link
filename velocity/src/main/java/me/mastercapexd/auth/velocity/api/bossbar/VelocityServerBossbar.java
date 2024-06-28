package me.mastercapexd.auth.velocity.api.bossbar;

import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.auth.api.server.player.ServerPlayer;

import me.mastercapexd.auth.server.adventure.AdventureServerBossbar;
import me.mastercapexd.auth.velocity.player.VelocityServerPlayer;
import net.kyori.adventure.bossbar.BossBar;

public class VelocityServerBossbar extends AdventureServerBossbar {

    public VelocityServerBossbar(ServerComponent component) {
        super(component);
    }

    @Override
    public void showBossBar(ServerPlayer player, BossBar bossBar) {
        player.as(VelocityServerPlayer.class).getPlayer().showBossBar(bossBar);
    }

    @Override
    public void hideBossBar(ServerPlayer player, BossBar bossBar) {
        player.as(VelocityServerPlayer.class).getPlayer().hideBossBar(bossBar);
    }

}
