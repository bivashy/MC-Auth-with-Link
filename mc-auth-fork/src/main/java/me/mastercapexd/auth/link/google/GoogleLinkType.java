package me.mastercapexd.auth.link.google;

import me.mastercapexd.auth.link.AbstractLinkType;
import me.mastercapexd.auth.link.message.Message.MessageBuilder;
import me.mastercapexd.auth.link.message.keyboard.IKeyboard.IKeyboardBuilder;
import me.mastercapexd.auth.link.message.keyboard.button.Button.ButtonBuilder;

public class GoogleLinkType extends AbstractLinkType {
	private static final GoogleLinkType INSTANCE = new GoogleLinkType();
	private static final String UNSUPPORTED_MESSAGE = "Can`t create builder with GoogleLinkType!";

	private GoogleLinkType() {
		super("GOOGLE");
	}

	public static GoogleLinkType getInstance() {
		return INSTANCE;
	}

	@Override
	public MessageBuilder newMessageBuilder() {
		throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
	}

	@Override
	public IKeyboardBuilder newKeyboardBuilder() {
		throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
	}
		
	@Override
	public ButtonBuilder newButtonBuilder() {
		throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
	}

	
}
