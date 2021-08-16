package me.mastercapexd.auth.config;

import java.util.Map;

import com.google.common.collect.Maps;

import me.mastercapexd.auth.Account;
import me.mastercapexd.auth.Messages;
import net.md_5.bungee.config.Configuration;

public class VKMessages implements Messages {

	private final Map<String, String> strings = Maps.newHashMap();
	public VKMessages(Configuration messages) {
		for (String key : messages.getKeys())
			strings.put(key, messages.getString(key));
	}

	@Override
	public String colorMessage(String message) {
		return message;
	}
	@Override
	public String getLegacyMessage(String key) {
		return strings.get(key);
	}

	public String getMessage(String key, Account account) {
		return getLegacyMessage(key).replaceAll("(?i)%name%", account.getName())
				.replaceAll("(?i)%nick%", account.getName()).replaceAll("(?i)%account_ip%", account.getLastIpAddress())
				.replaceAll("(?i)%vk_id%", String.valueOf(account.getVKId()));
	}

}
