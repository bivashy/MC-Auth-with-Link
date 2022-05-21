package me.mastercapexd.auth.config.message;

import me.mastercapexd.auth.config.message.context.MessageContext;

public interface Messages<T> {
	T getMessage(String key);

	T getMessage(String key, MessageContext context);

	String getStringMessage(String key, String defaultValue);

	Messages<T> getSubMessages(String key);

	T fromText(String text);

	default String getStringMessage(String key) {
		return getStringMessage(key, null);
	}

	default String formatString(String message) {
		return message;
	}
}