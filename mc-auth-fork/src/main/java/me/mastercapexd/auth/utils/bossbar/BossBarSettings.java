package me.mastercapexd.auth.utils.bossbar;

import com.ubivashka.config.annotations.ConfigField;
import com.ubivashka.config.annotations.ConverterType;
import com.ubivashka.config.processors.BungeeConfigurationHolder;

import net.md_5.bungee.config.Configuration;

public class BossBarSettings extends BungeeConfigurationHolder{
	@ConfigField(path = "use")
	private Boolean enabled = false;
	@ConfigField(path = "bar-color")
	private BarColor barColor = null;
	@ConfigField(path = "bar-style")
	private BarStyle barStyle = null;
	@ConfigField(path = "bar-text")
	@ConverterType("message-field")
	private String barText = null;

	public BossBarSettings(Configuration config) {
		init(config);
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

