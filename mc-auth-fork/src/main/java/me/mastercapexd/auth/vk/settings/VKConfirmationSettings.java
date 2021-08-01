package me.mastercapexd.auth.vk.settings;

import net.md_5.bungee.config.Configuration;

public class VKConfirmationSettings {
	private final int removeDelay;

	public VKConfirmationSettings(Configuration section) {
		removeDelay = section.getInt("remove-delay");

	}

	public int getRemoveDelay() {
		return removeDelay;
	}
}
