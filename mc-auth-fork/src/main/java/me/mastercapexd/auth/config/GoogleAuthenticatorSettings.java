package me.mastercapexd.auth.config;

import net.md_5.bungee.config.Configuration;

public class GoogleAuthenticatorSettings {
	private boolean enabled = false;

	public GoogleAuthenticatorSettings(Configuration section) {
		enabled = section.getBoolean("enabled");
	}

	public boolean isEnabled() {
		return enabled;
	}
}
