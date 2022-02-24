package me.mastercapexd.auth.link.user;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.LinkType;

public abstract class AbstractLinkUser implements LinkUser{
	protected final LinkType linkType;
	protected final Account account;
	
	public AbstractLinkUser(LinkType linkType,Account account) {
		this.linkType = linkType;
		this.account = account;
	}

	@Override
	public LinkType getLinkType() {
		return linkType;
	}

	@Override
	public Account getAccount() {
		return account;
	}
	
	
	
}
