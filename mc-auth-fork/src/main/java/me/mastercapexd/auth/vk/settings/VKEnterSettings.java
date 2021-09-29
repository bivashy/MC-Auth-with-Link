package me.mastercapexd.auth.vk.settings;

import net.md_5.bungee.config.Configuration;

public class VKEnterSettings {
	private final Integer enterDelay;
	private final boolean canToggleEnterConfirmation;

	public VKEnterSettings(Configuration section) {
		this.enterDelay = section.getInt("enter-delay");
		this.canToggleEnterConfirmation = section.getBoolean("can-toggle-enter");
	}

	public Integer getEnterDelay() {
		return enterDelay;
	}

	public boolean canToggleEnterConfirmation() {
		return canToggleEnterConfirmation;
	}
}
