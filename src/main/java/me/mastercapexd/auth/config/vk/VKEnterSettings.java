package me.mastercapexd.auth.config.vk;

import com.ubivashka.configuration.annotations.ConfigField;
import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.ConfigurationHolder;
import me.mastercapexd.auth.config.messenger.MessengerEnterSettings;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class VKEnterSettings implements ConfigurationHolder, MessengerEnterSettings {
	@ConfigField("enter-delay")
	private Integer enterDelay = 60;

	public VKEnterSettings(ConfigurationSectionHolder sectionHolder) {
		ProxyPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
	}

	@Override
	public int getEnterDelay() {
		return enterDelay;
	}
}
