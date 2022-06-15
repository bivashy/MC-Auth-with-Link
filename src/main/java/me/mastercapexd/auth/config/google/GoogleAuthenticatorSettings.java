package me.mastercapexd.auth.config.google;

import com.ubivashka.configuration.annotations.ConfigField;
import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.ConfigurationHolder;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class GoogleAuthenticatorSettings implements ConfigurationHolder {
	@ConfigField
	private boolean enabled = false;

	public GoogleAuthenticatorSettings(ConfigurationSectionHolder sectionHolder) {
		ProxyPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
	}

	public boolean isEnabled() {
		return enabled;
	}
}
