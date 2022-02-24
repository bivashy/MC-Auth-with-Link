package me.mastercapexd.auth.config;

import java.util.Map;

import com.google.common.collect.Maps;
import com.ubivashka.config.annotations.ConfigField;
import com.ubivashka.config.processors.BungeeConfigurationHolder;

import me.mastercapexd.auth.account.Account;
import net.md_5.bungee.config.Configuration;

public class VKButtonLabels extends BungeeConfigurationHolder{
	@ConfigField
	private Map<String, String> labels = Maps.newHashMap();

	public VKButtonLabels(Configuration messages) {
		init(messages);
	}

	public String getButtonLabel(String key) {
		return labels.get(key);
	}

	public String getButtonLabel(String key, Account account) {
		return getButtonLabel(key).replaceAll("(?i)%name%", account.getName())
				.replaceAll("(?i)%nick%", account.getName()).replaceAll("(?i)%account_ip%", account.getLastIpAddress());
	}
}
