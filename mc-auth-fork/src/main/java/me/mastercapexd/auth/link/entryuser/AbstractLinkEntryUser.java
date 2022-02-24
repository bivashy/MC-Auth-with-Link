package me.mastercapexd.auth.link.entryuser;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.link.user.AbstractLinkUser;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;

public abstract class AbstractLinkEntryUser extends AbstractLinkUser implements LinkEntryUser {
	protected final Long confirmationStartTime = System.currentTimeMillis();
	protected final LinkUserInfo linkUserInfo;
	protected boolean confirmed = false;

	public AbstractLinkEntryUser(LinkType linkType, Account account, LinkUserInfo linkUserInfo) {
		super(linkType, account);
		this.linkUserInfo = linkUserInfo;
	}

	public Long getConfirmationStartTime() {
		return confirmationStartTime;
	}

	@Override
	public LinkUserInfo getLinkUserInfo() {
		return linkUserInfo;
	}

	@Override
	public boolean isConfirmed() {
		return confirmed;
	}

	@Override
	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

}
