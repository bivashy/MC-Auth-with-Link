package me.mastercapexd.auth.utils.bossbar;

import net.md_5.bungee.config.Configuration;

public class BossBarSettings {
	private final boolean enabled;
	private final BarColor barColor;
	private final BarStyle barStyle;
	private final String barText;

	public BossBarSettings(Configuration config) {
		enabled = config.getBoolean("use");
		if (!enabled) {
			barColor = null;
			barStyle = null;
			barText = null;
			return;
		}
		barColor = BarColor.valueOf(config.getString("bar-color"));
		barStyle = BarStyle.valueOf(config.getString("bar-style"));
		barText = config.getString("bar-text");
	}

	public boolean isEnabled() {
		return enabled;
	}
	
	public BossBar createBossBar() {
		if (!enabled)
			return null;
		return new BossBar(barText, barColor, barStyle);
	}
}
