package me.mastercapexd.auth.vk.commands.dependencies;

import me.mastercapexd.auth.config.messages.Messages;
import me.mastercapexd.auth.config.messages.MessagesWrapper;
import me.mastercapexd.auth.config.messages.proxy.ProxyMessageContext;
import net.md_5.bungee.api.chat.BaseComponent;

public class ProxyVkMessagesWrapper extends MessagesWrapper<BaseComponent[], ProxyMessageContext> {

	public ProxyVkMessagesWrapper(Messages<BaseComponent[], ProxyMessageContext> messages) {
		super(messages);
	}

}
