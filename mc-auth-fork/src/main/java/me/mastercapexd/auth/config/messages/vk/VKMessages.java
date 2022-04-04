package me.mastercapexd.auth.config.messages.vk;

import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.messages.AbstractMessages;

public class VKMessages extends AbstractMessages<String, VKMessageContext> {
	public VKMessages(ConfigurationSectionHolder configurationSection) {
		super(configurationSection, "<br>");
	}

	@Override
	public String getMessage(String key) {
		return getStringMessage(key);
	}

	@Override
	public String getMessage(String key, VKMessageContext context) {
		return context.formatString(getMessage(key));
	}

	@Override
	protected AbstractMessages<String, VKMessageContext> createMessages(
			ConfigurationSectionHolder configurationSection) {
		return new VKMessages(configurationSection);
	}

}
