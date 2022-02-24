package me.mastercapexd.auth.vk.settings;

import com.ubivashka.config.annotations.ConfigField;
import com.ubivashka.config.processors.BungeeConfigurationHolder;

import net.md_5.bungee.config.Configuration;

public class VKRestoreSettings extends BungeeConfigurationHolder{
	@ConfigField(path = "code-length")
	private int codeLength = 6;

	public VKRestoreSettings(Configuration section) {
		init(section);
	}

	public int getCodeLength() {
		return codeLength;
	}
}
