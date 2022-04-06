package me.mastercapexd.auth.bungee.message;

import java.awt.Color;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.mastercapexd.auth.proxy.message.MultiProxyComponent;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class BungeeMultiProxyComponent implements MultiProxyComponent {
	private static final Pattern HEX_PATTERN = Pattern.compile("#([A-Fa-f0-9]){6}");
	private BaseComponent[] components;

	public BungeeMultiProxyComponent(BaseComponent[] components) {
		this.components = components;
	}

	public BungeeMultiProxyComponent(String legacyText) {
		this(TextComponent.fromLegacyText(applyColor(legacyText)));
	}

	@Override
	public String jsonText() {
		return ComponentSerializer.toString(components);
	}

	@Override
	public String legacyText() {
		return TextComponent.toLegacyText(components);
	}

	@Override
	public String plainText() {
		return ChatColor.stripColor(legacyText());
	}

	@Override
	public ProxyComponent[] getComponents() {
		return Arrays.stream(components).map(baseComponent -> new BungeeProxyComponent(baseComponent))
				.toArray(ProxyComponent[]::new);
	}
	
	public BaseComponent[] components() {
		return components;
	}

	private static String applyColor(String message) {
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
}
