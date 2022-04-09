package me.mastercapexd.auth.link.entryuser.vk;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.entryuser.AbstractLinkEntryUser;
import me.mastercapexd.auth.link.vk.VKLinkType;
import me.mastercapexd.auth.link.vk.VKLinkUserInfo;

public class VKLinkEntryUser extends AbstractLinkEntryUser {

	public VKLinkEntryUser(Account account) {
		super(VKLinkType.getInstance(), account, new VKLinkUserInfo(account.getLinkUsers().stream()
				.filter(VKLinkType.LINK_USER_FILTER).findFirst().orElse(null).getLinkUserInfo()));
	}

}
