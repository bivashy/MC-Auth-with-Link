package me.mastercapexd.auth.link.confirmation.info;

import me.mastercapexd.auth.link.user.info.AbstractLinkUserInfo;
import me.mastercapexd.auth.link.user.info.identificator.LinkUserIdentificator;

public abstract class AbstractLinkConfirmationInfo extends AbstractLinkUserInfo implements LinkConfirmationInfo {
	public AbstractLinkConfirmationInfo(LinkUserIdentificator userIdentificator) {
		super(userIdentificator);
	}
}
