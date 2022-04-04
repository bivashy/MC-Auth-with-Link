package me.mastercapexd.auth.config.messages;

public abstract class MessagesWrapper<T, C extends MessageContext> implements Messages<T, C> {
	private final Messages<T, C> messages;

	public MessagesWrapper(Messages<T, C> messages) {
		this.messages = messages;
	}

	@Override
	public T getMessage(String key) {
		return messages.getMessage(key);
	}

	@Override
	public T getMessage(String key, C context) {
		return messages.getMessage(key, context);
	}

	@Override
	public Messages<T, C> getSubMessages(String key) {
		return messages.getSubMessages(key);
	}

	@Override
	public String getStringMessage(String key) {
		return messages.getStringMessage(key);
	}

}
