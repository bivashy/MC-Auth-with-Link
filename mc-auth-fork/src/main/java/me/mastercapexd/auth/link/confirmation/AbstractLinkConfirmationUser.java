package me.mastercapexd.auth.link.confirmation;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.link.confirmation.info.LinkConfirmationInfo;
import me.mastercapexd.auth.link.user.AbstractLinkUser;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;

public abstract class AbstractLinkConfirmationUser extends AbstractLinkUser implements LinkConfirmationUser {
	private static final AuthPlugin PLUGIN = AuthPlugin.getInstance();
	private final Long linkTimeoutMillis = System.currentTimeMillis()
			+ PLUGIN.getConfig().getVKSettings().getConfirmationSettings().getRemoveDelay() * 1000;
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

	@Override
	public Long getLinkTimeoutMillis() {
		return linkTimeoutMillis;
	}
}
