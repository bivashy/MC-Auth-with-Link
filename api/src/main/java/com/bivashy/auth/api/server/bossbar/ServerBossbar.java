package com.bivashy.auth.api.server.bossbar;

import java.util.Collection;

import com.bivashy.auth.api.server.player.ServerPlayer;
import com.bivashy.auth.api.util.Castable;

public abstract class ServerBossbar implements Castable<ServerBossbar> {
    protected Style segmentStyle = Style.SOLID;
    protected Color color = Color.BLUE;
    protected float progress;
    protected String title;

    public ServerBossbar color(Color color) {
        this.color = color;
        return this;
    }

    public ServerBossbar style(Style segmentStyle) {
        this.segmentStyle = segmentStyle;
        return this;
    }

    public ServerBossbar progress(float progress) {
        if (progress < 0 || 1 < progress)
            throw new IllegalArgumentException("Bossbar progress must be between 0.0 and 1.0, but got " + progress);
        this.progress = progress;
        return this;
    }

    public ServerBossbar title(String title) {
        this.title = title;
        return this;
    }

    public abstract ServerBossbar send(ServerPlayer... viewers);

    public abstract ServerBossbar remove(ServerPlayer... viewers);

    public abstract ServerBossbar update();

    public abstract ServerBossbar removeAll();

    public abstract Collection<ServerPlayer> players();

    public enum Color {
        PINK, BLUE, RED, GREEN, YELLOW, PURPLE, WHITE
    }
    public enum Style {
        SOLID, SEGMENTED_6, SEGMENTED_10, SEGMENTED_12, SEGMENTED_20
    }
}
