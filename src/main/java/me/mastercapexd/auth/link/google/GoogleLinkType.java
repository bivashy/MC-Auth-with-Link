package me.mastercapexd.auth.link.google;

import com.google.common.base.Predicate;
import com.ubivaska.messenger.common.button.Button.ButtonBuilder;
import com.ubivaska.messenger.common.button.ButtonAction.ButtonActionBuilder;
import com.ubivaska.messenger.common.button.ButtonColor.ButtonColorBuilder;
import com.ubivaska.messenger.common.keyboard.Keyboard.KeyboardBuilder;
import com.ubivaska.messenger.common.message.Message.MessageBuilder;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.account.factories.AccountFactory;
import me.mastercapexd.auth.config.message.Messages;
import me.mastercapexd.auth.config.message.messenger.context.MessengerPlaceholderContext;
import me.mastercapexd.auth.config.messenger.MessengerSettings;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.link.user.LinkUser;
import me.mastercapexd.auth.link.user.info.identificator.LinkUserIdentificator;
import me.mastercapexd.auth.link.user.info.identificator.UserStringIdentificator;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.message.ProxyComponent;

public class GoogleLinkType implements LinkType {
	public static final Predicate<LinkUser> LINK_USER_FILTER = (linkUser) -> linkUser.getLinkType() == getInstance();

	private static final GoogleLinkType INSTANCE = new GoogleLinkType();
	private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();
	private static final LinkUserIdentificator DEFAULT_IDENTIFICATOR = new UserStringIdentificator(
			AccountFactory.DEFAULT_GOOGLE_KEY);

	private static final String CANNOT_CREATE_BUILDER_ERROR = "Can`t create builder with GoogleLinkType!";
	private static final String UNSUPPORTED_ERROR = "This method don`t supported by google link type";

	private GoogleLinkType() {
	}

	public static GoogleLinkType getInstance() {
		return INSTANCE;
	}

	@Override
	public MessageBuilder newMessageBuilder(String text) {
		throw new UnsupportedOperationException(CANNOT_CREATE_BUILDER_ERROR);
	}

	@Override
	public ButtonBuilder newButtonBuilder(String label) {
		throw new UnsupportedOperationException(CANNOT_CREATE_BUILDER_ERROR);
	}

	@Override
	public KeyboardBuilder newKeyboardBuilder() {
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
	public MessengerPlaceholderContext newMessageContext(Account account) {
		throw new UnsupportedOperationException(UNSUPPORTED_ERROR);
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

	@Override
	public LinkUserIdentificator getDefaultIdentificator() {
		return DEFAULT_IDENTIFICATOR;
	}

}
