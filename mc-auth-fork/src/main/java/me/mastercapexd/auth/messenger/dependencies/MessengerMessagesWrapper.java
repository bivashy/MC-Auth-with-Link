package me.mastercapexd.auth.messenger.dependencies;

import me.mastercapexd.auth.config.messages.MessageContext;
import me.mastercapexd.auth.config.messages.Messages;

public interface MessengerMessagesWrapper<T, C extends MessageContext> extends Messages<T, C> {
}
