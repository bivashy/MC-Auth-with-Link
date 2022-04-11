package me.mastercapexd.auth.config.messages.vk;

import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.messages.AbstractMessages;
import me.mastercapexd.auth.config.messages.MessageContext;
import me.mastercapexd.auth.config.messages.Messages;

public class VKMessages extends AbstractMessages<String> {
	public VKMessages(ConfigurationSectionHolder configurationSection) {
		super(configurationSection, "<br>");
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
		return new VKMessages(configurationSection);
	}

	@Override
	public String fromText(String text) {
		return text;
	}

}
