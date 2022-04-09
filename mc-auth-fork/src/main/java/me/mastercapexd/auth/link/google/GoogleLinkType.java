package me.mastercapexd.auth.link.google;

import com.google.common.base.Predicate;

import me.mastercapexd.auth.link.AbstractLinkType;
import me.mastercapexd.auth.link.message.Message.MessageBuilder;
import me.mastercapexd.auth.link.message.keyboard.IKeyboard.IKeyboardBuilder;
import me.mastercapexd.auth.link.message.keyboard.button.Button.ButtonBuilder;
import me.mastercapexd.auth.link.message.keyboard.button.ButtonAction.ButtonActionBuilder;
import me.mastercapexd.auth.link.message.keyboard.button.ButtonColor.ButtonColorBuilder;
import me.mastercapexd.auth.link.user.LinkUser;

public class GoogleLinkType extends AbstractLinkType {
	public static final Predicate<LinkUser> LINK_USER_FILTER = (linkUser) -> linkUser.getLinkType()==getInstance();
	public static final String NULL_KEY = "";
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

	@Override
	public ButtonColorBuilder newButtonColorBuilder() {
		throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
	}

	@Override
	public ButtonActionBuilder newButtonActionBuilder() {
		throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
	}

}
