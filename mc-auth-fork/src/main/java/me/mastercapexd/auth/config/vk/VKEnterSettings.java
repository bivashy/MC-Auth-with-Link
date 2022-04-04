package me.mastercapexd.auth.config.vk;

import com.ubivashka.configuration.annotations.ConfigField;
import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.ConfigurationHolder;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class VKEnterSettings implements ConfigurationHolder {
	@ConfigField("enter-delay")
	private Integer enterDelay = 60;
	@ConfigField("can-toggle-enter")
	private boolean canToggleEnterConfirmation = false;

	public VKEnterSettings(ConfigurationSectionHolder sectionHolder) {
		ProxyPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
	}

	public Integer getEnterDelay() {
		return enterDelay;
	}

	public boolean canToggleEnterConfirmation() {
		return canToggleEnterConfirmation;
	}
}
