package me.mastercapexd.auth.config.messages.bungee;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.mastercapexd.auth.config.messages.AbstractMessages;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;

public class BungeeMessages extends AbstractMessages<BaseComponent[], BungeeMessageContext> {

	private static final Pattern HEX_PATTERN = Pattern.compile("#([A-Fa-f0-9]){6}");

	public BungeeMessages(Configuration configurationSection) {
		super(configurationSection);
	}

	@Override
	public BaseComponent[] getMessage(String key) {
		return TextComponent.fromLegacyText(getStringMessage(key));
	}

	@Override
	public BaseComponent[] getMessage(String key, BungeeMessageContext context) {
		return TextComponent.fromLegacyText(context.formatString(getStringMessage(key)));
	}

	@Override
	public String formatString(String message) {
		return ChatColor.translateAlternateColorCodes('&', applyColor(message));
	}

	private String applyColor(String message) {
		Matcher matcher = HEX_PATTERN.matcher(message);
		while (matcher.find()) {
			final ChatColor hexColor = ChatColor
					.of(Color.decode(matcher.group().substring(0, matcher.group().length())));
			final String before = message.substring(0, matcher.start());
			final String after = message.substring(matcher.end());
			message = before + hexColor + after;
			matcher = HEX_PATTERN.matcher(message);
		}
		return message;
	}

	@Override
	protected AbstractMessages<BaseComponent[], BungeeMessageContext> createMessages(
			Configuration configurationSection) {
		return new BungeeMessages(configurationSection);
	}

}