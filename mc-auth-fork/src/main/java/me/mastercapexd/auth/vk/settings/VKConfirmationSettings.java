package me.mastercapexd.auth.vk.settings;

import com.ubivashka.config.annotations.ConfigField;
import com.ubivashka.config.processors.BungeeConfigurationHolder;

import net.md_5.bungee.config.Configuration;

public class VKConfirmationSettings extends BungeeConfigurationHolder {
	@ConfigField(path = "remove-delay")
	private int removeDelay = 120;
	@ConfigField(path = "code-length")
	private int codeLength = 6;

	public VKConfirmationSettings(Configuration section) {
		init(section);
	}

	public int getRemoveDelay() {
		return removeDelay;
	}

	public int getCodeLength() {
		return codeLength;
	}
}
