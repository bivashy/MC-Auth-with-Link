package me.mastercapexd.auth.link.confirmation;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.link.confirmation.info.LinkConfirmationInfo;
import me.mastercapexd.auth.link.user.AbstractLinkUser;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;

public abstract class AbstractLinkConfirmationUser extends AbstractLinkUser implements LinkConfirmationUser {
	private final LinkConfirmationInfo confirmationInfo;

	public AbstractLinkConfirmationUser(LinkType linkType, Account account, LinkConfirmationInfo confirmationInfo) {
		super(linkType, account);
		this.confirmationInfo = confirmationInfo;
	}

	@Override
	public LinkUserInfo getLinkUserInfo() {
		return confirmationInfo;
	}

	@Override
	public LinkConfirmationInfo getConfirmationInfo() {
		return confirmationInfo;
	}

}
