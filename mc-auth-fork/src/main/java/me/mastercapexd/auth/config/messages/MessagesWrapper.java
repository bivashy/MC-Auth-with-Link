package me.mastercapexd.auth.config.messages;

public abstract class MessagesWrapper<T> implements Messages<T> {
	private final Messages<T> messages;

	public MessagesWrapper(Messages<T> messages) {
		this.messages = messages;
	}

	@Override
	public T getMessage(String key) {
		return messages.getMessage(key);
	}

	@Override
	public T getMessage(String key, MessageContext context) {
		return messages.getMessage(key, context);
	}

	@Override
	public Messages<T> getSubMessages(String key) {
		return messages.getSubMessages(key);
	}

	@Override
	public String getStringMessage(String key) {
		return messages.getStringMessage(key);
	}

}
