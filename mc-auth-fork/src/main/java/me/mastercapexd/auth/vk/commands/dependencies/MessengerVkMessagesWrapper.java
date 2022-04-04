package me.mastercapexd.auth.vk.commands.dependencies;

import me.mastercapexd.auth.config.messages.Messages;
import me.mastercapexd.auth.config.messages.MessagesWrapper;
import me.mastercapexd.auth.config.messages.vk.VKMessageContext;
import me.mastercapexd.auth.messenger.dependencies.MessengerMessagesWrapper;

public class MessengerVkMessagesWrapper extends MessagesWrapper<String, VKMessageContext>
		implements MessengerMessagesWrapper<String, VKMessageContext> {

	public MessengerVkMessagesWrapper(Messages<String, VKMessageContext> messages) {
		super(messages);
	}

}
