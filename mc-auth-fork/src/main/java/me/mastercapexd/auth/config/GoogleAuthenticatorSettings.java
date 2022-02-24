package me.mastercapexd.auth.config;

import com.ubivashka.config.annotations.ConfigField;
import com.ubivashka.config.processors.BungeeConfigurationHolder;

import net.md_5.bungee.config.Configuration;

public class GoogleAuthenticatorSettings extends BungeeConfigurationHolder{
	@ConfigField
	private boolean enabled = false;

	public GoogleAuthenticatorSettings(Configuration section) {
		init(section);
	}

	public boolean isEnabled() {
		return enabled;
	}
}
