package me.mastercapexd.auth.config.messenger;

import com.ubivashka.configuration.annotations.ConfigField;
import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.ConfigurationHolder;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class DefaultRestoreSettings implements ConfigurationHolder, MessengerRestoreSettings {
	@ConfigField("code-length")
	private int codeLength = 6;
	@ConfigField("code-characters")
	private String codeCharacters = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public DefaultRestoreSettings(ConfigurationSectionHolder sectionHolder) {
		ProxyPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
	}

	@Override
	public int getCodeLength() {
		return codeLength;
	}

	@Override
	public String getCodeCharacters() {
		return codeCharacters;
	}
}
