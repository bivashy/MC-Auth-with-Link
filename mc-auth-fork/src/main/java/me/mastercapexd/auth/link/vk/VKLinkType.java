package me.mastercapexd.auth.link.vk;

import com.google.common.base.Predicate;

import me.mastercapexd.auth.account.factories.AccountFactory;
import me.mastercapexd.auth.config.messages.Messages;
import me.mastercapexd.auth.config.messenger.MessengerSettings;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.link.message.Message.MessageBuilder;
import me.mastercapexd.auth.link.message.keyboard.IKeyboard.IKeyboardBuilder;
import me.mastercapexd.auth.link.message.keyboard.button.Button.ButtonBuilder;
import me.mastercapexd.auth.link.message.keyboard.button.ButtonAction.ButtonActionBuilder;
import me.mastercapexd.auth.link.message.keyboard.button.ButtonColor.ButtonColorBuilder;
import me.mastercapexd.auth.link.message.vk.VKButton;
import me.mastercapexd.auth.link.message.vk.VKButtonAction.VKButtonActionBuilder;
import me.mastercapexd.auth.link.message.vk.VKButtonColor.VKButtonColorBuilder;
import me.mastercapexd.auth.link.message.vk.VKKeyboard;
import me.mastercapexd.auth.link.message.vk.VKMessage;
import me.mastercapexd.auth.link.user.LinkUser;
import me.mastercapexd.auth.link.user.info.identificator.LinkUserIdentificator;
import me.mastercapexd.auth.link.user.info.identificator.UserNumberIdentificator;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.message.ProxyComponent;

public class VKLinkType implements LinkType {
	public static final Predicate<LinkUser> LINK_USER_FILTER = (linkUser) -> linkUser.getLinkType() == getInstance();
	private static final VKLinkType INSTANCE = new VKLinkType();
	private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();
	private static final LinkUserIdentificator DEFAULT_IDENTIFICATOR = new UserNumberIdentificator(
			AccountFactory.DEFAULT_VK_ID);

	private VKLinkType() {
	}

	public static VKLinkType getInstance() {
		return INSTANCE;
	}

	@Override
	public MessageBuilder newMessageBuilder(String text) {
		return new VKMessage(text).new VKMessageBuilder();
	}

	@Override
	public ButtonBuilder newButtonBuilder(String label) {
		return new VKButton(label).new VKButtonBuilder();
	}

	@Override
	public IKeyboardBuilder newKeyboardBuilder() {
		return new VKKeyboard().new VKKeyboardBuilder();
	}

	@Override
	public ButtonActionBuilder newButtonActionBuilder() {
		return new VKButtonActionBuilder();
	}

	@Override
	public ButtonColorBuilder newButtonColorBuilder() {
		return new VKButtonColorBuilder();
	}

	@Override
	public Messages<ProxyComponent> getProxyMessages() {
		return PLUGIN.getConfig().getProxyMessages().getSubMessages("vk");
	}

	@Override
	public Messages<String> getLinkMessages() {
		return PLUGIN.getConfig().getVKSettings().getMessages();
	}

	@Override
	public MessengerSettings getSettings() {
		return PLUGIN.getConfig().getVKSettings();
	}

	@Override
	public LinkUserIdentificator getDefaultIdentificator() {
		return DEFAULT_IDENTIFICATOR;
	}

}
