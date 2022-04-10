package me.mastercapexd.auth.link.vk;

import com.google.common.base.Predicate;

import me.mastercapexd.auth.config.messages.Messages;
import me.mastercapexd.auth.link.AbstractLinkType;
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
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.message.ProxyComponent;

public class VKLinkType extends AbstractLinkType {
	public static final Predicate<LinkUser> LINK_USER_FILTER = (linkUser) -> linkUser.getLinkType() == getInstance();
	private static final VKLinkType INSTANCE = new VKLinkType();
	private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();

	private VKLinkType() {
		super("VK");
	}

	public static VKLinkType getInstance() {
		return INSTANCE;
	}

	@Override
	public MessageBuilder newMessageBuilder() {
		return VKMessage.newBuilder("");
	}

	@Override
	public IKeyboardBuilder newKeyboardBuilder() {
		return VKKeyboard.newBuilder();
	}

	@Override
	public ButtonBuilder newButtonBuilder() {
		return VKButton.newBuilder("");
	}

	@Override
	public ButtonColorBuilder newButtonColorBuilder() {
		return VKButtonColorBuilder.getInstance();
	}

	@Override
	public ButtonActionBuilder newButtonActionBuilder() {
		return VKButtonActionBuilder.getInstance();
	}

	@Override
	public Messages<ProxyComponent> getProxyMessages() {
		return PLUGIN.getConfig().getProxyMessages().getSubMessages("vk");
	}

	@Override
	public Messages<String> getLinkMessages() {
		return PLUGIN.getConfig().getVKSettings().getVKMessages();
	}

}
