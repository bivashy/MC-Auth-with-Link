package me.mastercapexd.auth.link;

import me.mastercapexd.auth.link.message.Message.MessageBuilder;
import me.mastercapexd.auth.link.message.keyboard.IKeyboard.IKeyboardBuilder;
import me.mastercapexd.auth.link.message.keyboard.button.Button.ButtonBuilder;

public interface LinkType {
	String getLinkName();
	
	MessageBuilder newMessageBuilder();
	
	IKeyboardBuilder newKeyboardBuilder();
	
	ButtonBuilder newButtonBuilder();
}
