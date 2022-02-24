package me.mastercapexd.auth.config.messages;

public interface Messages<T, C extends MessageContext> {
	T getMessage(String key);

	T getMessage(String key, C context);
	
	Messages<T,C> getSubMessages(String key);

	String getStringMessage(String key);

	default String formatString(String message) {
		return message;
	}
}