package me.mastercapexd.auth.utils.bossbar;

import java.nio.charset.Charset;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.chat.ComponentSerializer;

/**
 * @author theminecoder
 * @version 1.0
 */
public class BossBar {

    private static final AtomicInteger barID = new AtomicInteger(1);

    private final UUID uuid;
    private String title;
    private float progress = 1;
    private BarColor barColor;
    private BarStyle barStyle;

    private String compiledTitle;

    private boolean visible = true;

    private final Set<ProxiedPlayer> players = Sets.newHashSet();

    public BossBar(String title, BarColor barColor, BarStyle barStyle) {
        this.uuid = UUID.nameUUIDFromBytes(("BBB:" + barID.getAndIncrement()).getBytes(Charset.forName("UTF-8")));
        this.title = title;
        this.compiledTitle = ComponentSerializer.toString(new TextComponent(this.title));
        this.barColor = barColor;
        this.barStyle = barStyle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.compiledTitle = ComponentSerializer.toString(new TextComponent(this.title));

        if (visible) {
            net.md_5.bungee.protocol.packet.BossBar packet = new net.md_5.bungee.protocol.packet.BossBar(uuid, 3);
            packet.setTitle(this.compiledTitle);
            this.players.forEach(player -> player.unsafe().sendPacket(packet));
        }
    }

    public BarColor getBarColor() {
        return barColor;
    }

    public void setBarColor(BarColor barColor) {
        this.barColor = barColor;

        if (visible) {
            net.md_5.bungee.protocol.packet.BossBar packet = new net.md_5.bungee.protocol.packet.BossBar(uuid, 4);
            packet.setColor(this.barColor.ordinal());
            packet.setDivision(this.barStyle.ordinal());
            this.players.forEach(player -> player.unsafe().sendPacket(packet));
        }
    }

    public BarStyle getBarStyle() {
        return barStyle;
    }

    public void setBarStyle(BarStyle barStyle) {
        this.barStyle = barStyle;

        if (visible) {
            net.md_5.bungee.protocol.packet.BossBar packet = new net.md_5.bungee.protocol.packet.BossBar(uuid, 4);
            packet.setColor(this.barColor.ordinal());
            packet.setDivision(this.barStyle.ordinal());
            this.players.forEach(player -> player.unsafe().sendPacket(packet));
        }
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;

        if (visible) {
            net.md_5.bungee.protocol.packet.BossBar packet = new net.md_5.bungee.protocol.packet.BossBar(uuid, 2);
            packet.setHealth(this.progress);
            this.players.forEach(player -> player.unsafe().sendPacket(packet));
        }
    }

    public boolean hasPlayer(ProxiedPlayer player) {
        return this.players.contains(player);
    }

    public void addPlayer(ProxiedPlayer player) {
        this.players.add(player);

        if (visible && player.isConnected()) {
            player.unsafe().sendPacket(getAddPacket());
        }
    }

    public void removePlayer(ProxiedPlayer player) {
        this.players.remove(player);

        if (visible && player.isConnected()) {
            player.unsafe().sendPacket(getRemovePacket());
        }
    }

    public void removeAll() {
        this.players.stream().collect(Collectors.toSet()).forEach(this::removePlayer);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        if (visible == this.visible) {
            return;
        }

        this.visible = visible;

        net.md_5.bungee.protocol.packet.BossBar packet = visible ? getAddPacket() : getRemovePacket();
        this.players.forEach(player -> player.unsafe().sendPacket(packet));
    }

    private net.md_5.bungee.protocol.packet.BossBar getAddPacket() {
        net.md_5.bungee.protocol.packet.BossBar packet = new net.md_5.bungee.protocol.packet.BossBar(uuid, 0);
        packet.setTitle(this.compiledTitle);
        packet.setColor(this.barColor.ordinal());
        packet.setDivision(this.barStyle.ordinal());
        packet.setHealth(this.progress);
        return packet;
    }

    private net.md_5.bungee.protocol.packet.BossBar getRemovePacket() {
        return new net.md_5.bungee.protocol.packet.BossBar(uuid, 1);
    }

}
