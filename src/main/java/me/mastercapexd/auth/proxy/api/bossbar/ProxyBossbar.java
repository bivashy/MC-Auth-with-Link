package me.mastercapexd.auth.proxy.api.bossbar;

import java.util.Collection;

import me.mastercapexd.auth.function.Castable;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;

/**
 * Proxy bossbar for bukkit,bungee,velocity. This bossbar will not automatically
 * update on info change. You will need to call {@linkplain #update()} method to
 * the display changes for the viewers.
 *
 */
public abstract class ProxyBossbar implements Castable<ProxyBossbar> {

	protected ProxyBossbar.Style segmentStyle;
	protected ProxyBossbar.Color color;
	protected float progress;
	protected String title;

	public ProxyBossbar color(ProxyBossbar.Color color) {
		this.color = color;
		return this;
	}

	public ProxyBossbar style(ProxyBossbar.Style segmentStyle) {
		this.segmentStyle = segmentStyle;
		return this;
	}

	public ProxyBossbar progress(float progress) {
		if (progress < 0 || 1 < progress)
			throw new IllegalArgumentException("Bossbar progress must be between 0.0 and 1.0, but got " + progress);
		this.progress = progress;
		return this;
	}

	public ProxyBossbar title(String title) {
		this.title = title;
		return this;
	}

	public abstract ProxyBossbar send(ProxyPlayer... viewers);

	public abstract ProxyBossbar remove(ProxyPlayer... viewers);

	public abstract ProxyBossbar update();

	public abstract ProxyBossbar removeAll();

	public abstract Collection<ProxyPlayer> players();

	public enum Color {
		PINK, BLUE, RED, GREEN, YELLOW, PURPLE, WHITE
	}

	public enum Style {
		SOLID, SEGMENTED_6, SEGMENTED_10, SEGMENTED_12, SEGMENTED_20
	}
}
