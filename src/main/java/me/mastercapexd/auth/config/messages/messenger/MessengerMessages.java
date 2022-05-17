package me.mastercapexd.auth.config.messages.messenger;

import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.messages.AbstractMessages;
import me.mastercapexd.auth.config.messages.MessageContext;
import me.mastercapexd.auth.config.messages.Messages;

public class MessengerMessages extends AbstractMessages<String> {
	private final String delimiter;

	public MessengerMessages(ConfigurationSectionHolder configurationSection, String delimiter) {
		super(configurationSection, delimiter);
		this.delimiter = delimiter;
	}

	@Override
	public String getMessage(String key) {
		return getStringMessage(key);
	}

	@Override
	public String getMessage(String key, MessageContext context) {
		return context.formatString(getMessage(key));
	}

	@Override
	protected Messages<String> createMessages(ConfigurationSectionHolder configurationSection) {
		return new MessengerMessages(configurationSection, delimiter);
	}

	@Override
	public String fromText(String text) {
		return text;
	}
}
