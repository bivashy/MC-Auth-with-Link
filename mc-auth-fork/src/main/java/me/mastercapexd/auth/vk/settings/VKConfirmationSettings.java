package me.mastercapexd.auth.vk.settings;

import net.md_5.bungee.config.Configuration;

public class VKConfirmationSettings {
	private final int removeDelay,codeLength;

	public VKConfirmationSettings(Configuration section) {
		removeDelay = section.getInt("remove-delay");
		codeLength = section.getInt("code-length");
	}

	public int getRemoveDelay() {
		return removeDelay;
	}

	public int getCodeLength() {
		return codeLength;
	}
}
