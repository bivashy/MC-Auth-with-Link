package me.mastercapexd.auth.link.vk;

import com.google.common.base.Predicate;

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

public class VKLinkType extends AbstractLinkType {
	private static final VKLinkType INSTANCE = new VKLinkType();

	private VKLinkType() {
		super("VK");
	}

	/**
	 * @return predicate that checks if ILinkType of ILinkUser equals to VKLinkType
	 */
	public static Predicate<LinkUser> getLinkUserPredicate() {
		return (linkUser) -> linkUser.getLinkType().equals(getInstance());
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

}
