package me.mastercapexd.auth.config.vk;

import com.ubivashka.configuration.annotations.ConfigField;
import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.ConfigurationHolder;
import me.mastercapexd.auth.config.messenger.MessengerRestoreSettings;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class VKRestoreSettings implements ConfigurationHolder, MessengerRestoreSettings {
	@ConfigField("code-length")
	private int codeLength = 6;

	public VKRestoreSettings(ConfigurationSectionHolder sectionHolder) {
		ProxyPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
	}

	@Override
	public int getCodeLength() {
		return codeLength;
	}
}
