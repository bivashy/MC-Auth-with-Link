package me.mastercapexd.auth.link.google;

import com.google.common.base.Predicate;

import me.mastercapexd.auth.account.factories.AccountFactory;
import me.mastercapexd.auth.config.messages.Messages;
import me.mastercapexd.auth.config.messenger.MessengerSettings;
import me.mastercapexd.auth.link.AbstractLinkType;
import me.mastercapexd.auth.link.message.Message.MessageBuilder;
import me.mastercapexd.auth.link.message.keyboard.IKeyboard.IKeyboardBuilder;
import me.mastercapexd.auth.link.message.keyboard.button.Button.ButtonBuilder;
import me.mastercapexd.auth.link.message.keyboard.button.ButtonAction.ButtonActionBuilder;
import me.mastercapexd.auth.link.message.keyboard.button.ButtonColor.ButtonColorBuilder;
import me.mastercapexd.auth.link.user.LinkUser;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.message.ProxyComponent;

public class GoogleLinkType extends AbstractLinkType {
	public static final Predicate<LinkUser> LINK_USER_FILTER = (linkUser) -> linkUser.getLinkType() == getInstance();
	public static final String NULL_KEY = AccountFactory.DEFAULT_GOOGLE_KEY;

	private static final GoogleLinkType INSTANCE = new GoogleLinkType();
	private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();

	private static final String CANNOT_CREATE_BUILDER_ERROR = "Can`t create builder with GoogleLinkType!";
	private static final String UNSUPPORTED_ERROR = "This method don`t supported by google link type";

	private GoogleLinkType() {
		super("GOOGLE");
	}

	public static GoogleLinkType getInstance() {
		return INSTANCE;
	}

	@Override
	public MessageBuilder newMessageBuilder() {
		throw new UnsupportedOperationException(CANNOT_CREATE_BUILDER_ERROR);
	}

	@Override
	public IKeyboardBuilder newKeyboardBuilder() {
		throw new UnsupportedOperationException(CANNOT_CREATE_BUILDER_ERROR);
	}

	@Override
	public ButtonBuilder newButtonBuilder() {
		throw new UnsupportedOperationException(CANNOT_CREATE_BUILDER_ERROR);
	}

	@Override
	public ButtonColorBuilder newButtonColorBuilder() {
		throw new UnsupportedOperationException(CANNOT_CREATE_BUILDER_ERROR);
	}

	@Override
	public ButtonActionBuilder newButtonActionBuilder() {
		throw new UnsupportedOperationException(CANNOT_CREATE_BUILDER_ERROR);
	}

	@Override
	public Messages<ProxyComponent> getProxyMessages() {
		return PLUGIN.getConfig().getProxyMessages().getSubMessages("google");
	}

	@Override
	public Messages<String> getLinkMessages() {
		throw new UnsupportedOperationException(UNSUPPORTED_ERROR);
	}

	@Override
	public MessengerSettings getSettings() {
		throw new UnsupportedOperationException(UNSUPPORTED_ERROR);
	}

}
