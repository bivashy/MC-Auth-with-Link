package me.mastercapexd.auth.vk.settings;

import com.ubivashka.config.annotations.ConfigField;
import com.ubivashka.config.processors.BungeeConfigurationHolder;

import net.md_5.bungee.config.Configuration;

public class VKEnterSettings extends BungeeConfigurationHolder{
	@ConfigField(path = "enter-delay")
	private Integer enterDelay = 60;
	@ConfigField(path = "can-toggle-enter")
	private boolean canToggleEnterConfirmation = false;

	public VKEnterSettings(Configuration section) {
		init(section);
	}

	public Integer getEnterDelay() {
		return enterDelay;
	}

	public boolean canToggleEnterConfirmation() {
		return canToggleEnterConfirmation;
	}
}
