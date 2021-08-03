package me.mastercapexd.auth.vk.settings;

import net.md_5.bungee.config.Configuration;

public class VKRestoreSettings {
	private final int codeLength;

	public VKRestoreSettings(Configuration section) {
		this.codeLength = section.getInt("code-length");
	}

	public int getCodeLength() {
		return codeLength;
	}
}
