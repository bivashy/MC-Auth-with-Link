package me.mastercapexd.auth.link.vk;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.user.AbstractLinkUser;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;

public class VKLinkUser extends AbstractLinkUser {
	private VKLinkUserInfo linkInfoAccount;

	public VKLinkUser(Account account, Integer vkId, boolean confirmationEnabled) {
		super(VKLinkType.getInstance(), account);
		this.linkInfoAccount = new VKLinkUserInfo(vkId,confirmationEnabled);
	}

	@Override
	public LinkUserInfo getLinkUserInfo() {
		return linkInfoAccount;
	}

}
