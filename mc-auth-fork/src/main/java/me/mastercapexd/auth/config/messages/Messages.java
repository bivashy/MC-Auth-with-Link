package me.mastercapexd.auth.config.messages;

public interface Messages<T> {
	T getMessage(String key);
	
	T getMessage(String key, MessageContext context);

	Messages<T> getSubMessages(String key);
	
	String getStringMessage(String key);

	String getStringMessage(String key,String defaultValue);

	T fromText(String text);
	
	default String formatString(String message) {
		return message;
	}
}