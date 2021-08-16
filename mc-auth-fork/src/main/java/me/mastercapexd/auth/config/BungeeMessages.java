package me.mastercapexd.auth.config;

import java.awt.Color;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Maps;

import me.mastercapexd.auth.Messages;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;

public class BungeeMessages implements Messages {

	private final Map<String, String> strings = Maps.newHashMap();
	private final Pattern hexPattern = Pattern.compile("#([A-Fa-f0-9]){6}");

	public BungeeMessages(Configuration messages) {
		for (String key : messages.getKeys())
			addMessage(key, messages.getString(key));
	}

	public void addMessage(String path, String message) {
		strings.put(path, colorMessage(message));
	}

	@Override
	public String colorMessage(String message) {
		return applyColor(ChatColor.translateAlternateColorCodes('&', message));
	}

	@Override
	public String getLegacyMessage(String key) {
		return strings.get(key);
	}

	private String applyColor(String message) {
		Matcher matcher = hexPattern.matcher(message);
		while (matcher.find()) {
			final ChatColor hexColor = ChatColor
					.of(Color.decode(matcher.group().substring(0, matcher.group().length())));
			final String before = message.substring(0, matcher.start());
			final String after = message.substring(matcher.end());
			message = before + hexColor + after;
			matcher = hexPattern.matcher(message);
		}
		return message;
	}
}