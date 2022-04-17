package me.mastercapexd.auth.config.vk;

import java.util.Map;

import com.google.common.collect.Maps;
import com.ubivashka.configuration.annotations.ConfigField;
import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.config.ConfigurationHolder;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class VKButtonLabels implements ConfigurationHolder {
	@ConfigField
	private Map<String, String> labels = Maps.newHashMap();

	public VKButtonLabels(ConfigurationSectionHolder sectionHolder) {
		ProxyPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
		System.out.println("labels " + labels);
	}

	public String getButtonLabel(String key) {
		return labels.get(key);
	}

	public String getButtonLabel(String key, Account account) {
		return getButtonLabel(key).replaceAll("(?i)%name%", account.getName())
				.replaceAll("(?i)%nick%", account.getName()).replaceAll("(?i)%account_ip%", account.getLastIpAddress());
	}
}
