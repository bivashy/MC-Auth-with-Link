package me.mastercapexd.auth.config;

import java.util.Map;

import com.google.common.collect.Maps;

import me.mastercapexd.auth.Messages;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;

public class BungeeMessages implements Messages {

	private final Map<String, String> strings = Maps.newHashMap();

	public BungeeMessages(Configuration messages) {
		for (String key : messages.getKeys())
			strings.put(key, ChatColor.translateAlternateColorCodes('&', messages.getString(key)));
	}

	@Override
	public String getLegacyMessage(String key) {
		return strings.get(key);
	}
}