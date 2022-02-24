package me.mastercapexd.auth.link.vk;

import com.google.common.base.Predicate;

import me.mastercapexd.auth.link.AbstractLinkType;
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

}
