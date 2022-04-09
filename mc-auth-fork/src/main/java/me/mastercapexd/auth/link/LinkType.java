package me.mastercapexd.auth.link;

import me.mastercapexd.auth.config.messages.Messages;
import me.mastercapexd.auth.link.message.Message.MessageBuilder;
import me.mastercapexd.auth.link.message.keyboard.IKeyboard.IKeyboardBuilder;
import me.mastercapexd.auth.link.message.keyboard.button.Button.ButtonBuilder;
import me.mastercapexd.auth.link.message.keyboard.button.ButtonAction.ButtonActionBuilder;
import me.mastercapexd.auth.link.message.keyboard.button.ButtonColor.ButtonColorBuilder;
import me.mastercapexd.auth.proxy.message.ProxyComponent;

public interface LinkType {
	/**
	 * Returns section of messages in proxy messages
	 * 
	 * @return messages in proxy messages
	 */
	Messages<ProxyComponent> getProxyMessages();

	/**
	 * Returns link messages that uses in social site, for example vk message, or telegram messages
	 * 
	 * @return
	 */
	Messages<String> getLinkMessages();

	String getLinkName();

	MessageBuilder newMessageBuilder();

	IKeyboardBuilder newKeyboardBuilder();

	ButtonBuilder newButtonBuilder();

	ButtonColorBuilder newButtonColorBuilder();

	ButtonActionBuilder newButtonActionBuilder();
}
