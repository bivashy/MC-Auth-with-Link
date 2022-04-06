package me.mastercapexd.auth.config.messages.proxy;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.messages.AbstractMessages;
import me.mastercapexd.auth.config.messages.MessageContext;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class ProxyMessages extends AbstractMessages<BaseComponent[]> {

	private static final Pattern HEX_PATTERN = Pattern.compile("#([A-Fa-f0-9]){6}");

	public ProxyMessages(ConfigurationSectionHolder configurationSection) {
		super(configurationSection);
	}

	@Override
	public BaseComponent[] getMessage(String key) {
		return TextComponent.fromLegacyText(getStringMessage(key));
	}

	@Override
	public BaseComponent[] getMessage(String key, MessageContext context) {
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
	protected AbstractMessages<BaseComponent[]> createMessages(
			ConfigurationSectionHolder configurationSection) {
		return new ProxyMessages(configurationSection);
	}

	@Override
	public BaseComponent[] fromText(String text) {
		return TextComponent.fromLegacyText(text);
	}

}