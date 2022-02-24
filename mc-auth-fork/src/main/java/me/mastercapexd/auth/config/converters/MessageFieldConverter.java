package me.mastercapexd.auth.config.converters;

import java.awt.Color;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ubivashka.config.contexts.BungeeConfigurationContext;
import com.ubivashka.config.converters.BungeeConfigurationListConverter;
import com.ubivashka.config.holders.IConfigurationSectionHolder;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;

public class MessageFieldConverter extends BungeeConfigurationListConverter<String, String> {
	private static final Pattern HEX_PATTERN = Pattern.compile("#([A-Fa-f0-9]){6}");

	public MessageFieldConverter() {
		super(String.class);
	}

	@Override
	protected String valueToEntity(BungeeConfigurationContext context, String valueObject) {
		return formatString(valueObject);
	}

	@Override
	protected String getConfigurationValue(BungeeConfigurationContext context) {
		IConfigurationSectionHolder<Configuration> sectionHolder = context.getConfigurationSectionHolder();
		if (!sectionHolder.isString(context.getConfigurationPath())
				&& !sectionHolder.isCollection(context.getConfigurationPath()))
			return null;
		return sectionHolder.getString(context.getConfigurationPath());
	}

	@Override
	protected List<String> getConfigurationValues(BungeeConfigurationContext context) {
		IConfigurationSectionHolder<Configuration> sectionHolder = context.getConfigurationSectionHolder();
		if (!sectionHolder.isCollection(context.getConfigurationPath()))
			return null;
		List<String> stringList = sectionHolder.getList(context.getConfigurationPath());
		return stringList;
	}

	private String formatString(String message) {
		return applyColor(ChatColor.translateAlternateColorCodes('&', message));
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

}
