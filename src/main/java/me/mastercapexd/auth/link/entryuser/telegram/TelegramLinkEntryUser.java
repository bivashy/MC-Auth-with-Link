package me.mastercapexd.auth.link.entryuser.telegram;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.entryuser.AbstractLinkEntryUser;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;
import me.mastercapexd.auth.link.vk.VKLinkType;

public class TelegramLinkEntryUser extends AbstractLinkEntryUser{

	public TelegramLinkEntryUser(Account account, LinkUserInfo linkUserInfo) {
		super(VKLinkType.getInstance(), account, linkUserInfo);
	}

	public TelegramLinkEntryUser(Account account) {
		this(account, account.findFirstLinkUser(VKLinkType.LINK_USER_FILTER).get().getLinkUserInfo());
	}
	
}
