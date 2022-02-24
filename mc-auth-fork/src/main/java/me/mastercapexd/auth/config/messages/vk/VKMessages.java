package me.mastercapexd.auth.config.messages.vk;

import me.mastercapexd.auth.config.messages.AbstractMessages;
import net.md_5.bungee.config.Configuration;

public class VKMessages extends AbstractMessages<String, VKMessageContext> {
	public VKMessages(Configuration configurationSection) {
		super(configurationSection,"<br>");
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
	protected AbstractMessages<String, VKMessageContext> createMessages(Configuration configurationSection) {
		return new VKMessages(configurationSection);
	}

}
