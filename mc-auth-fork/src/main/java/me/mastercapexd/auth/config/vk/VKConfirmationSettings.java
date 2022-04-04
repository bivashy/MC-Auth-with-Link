package me.mastercapexd.auth.config.vk;

import com.ubivashka.configuration.annotations.ConfigField;
import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.ConfigurationHolder;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class VKConfirmationSettings implements ConfigurationHolder {
	@ConfigField("remove-delay")
	private int removeDelay = 120;
	@ConfigField("code-length")
	private int codeLength = 6;

	public VKConfirmationSettings(ConfigurationSectionHolder sectionHolder) {
		ProxyPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
	}

	public int getRemoveDelay() {
		return removeDelay;
	}

	public int getCodeLength() {
		return codeLength;
	}
}
