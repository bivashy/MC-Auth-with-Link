package me.mastercapexd.auth.config.messenger;

import com.ubivashka.configuration.annotations.ConfigField;
import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.ConfigurationHolder;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class DefaultConfirmationSettings implements ConfigurationHolder, MessengerConfirmationSettings {
	@ConfigField("remove-delay")
	private int removeDelay = 120;
	@ConfigField("code-length")
	private int codeLength = 6;
	@ConfigField("can-toggle")
	private boolean canToggleConfirmation = false;
	@ConfigField("code-characters")
	private String codeCharacters = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public DefaultConfirmationSettings(ConfigurationSectionHolder sectionHolder) {
		ProxyPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
	}

	@Override
	public int getRemoveDelay() {
		return removeDelay;
	}

	@Override
	public int getCodeLength() {
		return codeLength;
	}

	@Override
	public boolean canToggleConfirmation() {
		return canToggleConfirmation;
	}

	@Override
	public String getCodeCharacters() {
		return codeCharacters;
	}
}
