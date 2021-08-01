package me.mastercapexd.auth.vk.settings;

import net.md_5.bungee.config.Configuration;

public class VKEnterSettings {
	private final Integer enterDelay;
	public VKEnterSettings(Configuration section) {
		this.enterDelay=section.getInt("enter-delay");
	}
	public Integer getEnterDelay() {
		return enterDelay;
	}
}
