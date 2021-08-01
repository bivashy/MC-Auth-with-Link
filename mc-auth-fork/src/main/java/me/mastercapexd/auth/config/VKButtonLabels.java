package me.mastercapexd.auth.config;

import java.util.Map;

import com.google.common.collect.Maps;

import me.mastercapexd.auth.Account;
import net.md_5.bungee.config.Configuration;

public class VKButtonLabels {
	private final Map<String, String> labels = Maps.newHashMap();

	public VKButtonLabels(Configuration messages) {
		for (String key : messages.getKeys())
			labels.put(key, messages.getString(key));
	}

	public String getButtonLabel(String key) {
		return labels.get(key);
	}

	public String getButtonLabel(String key, Account account) {
		return getButtonLabel(key).replaceAll("(?i)%name%", account.getName())
				.replaceAll("(?i)%nick%", account.getName()).replaceAll("(?i)%account_ip%", account.getLastIpAddress());
	}
}
