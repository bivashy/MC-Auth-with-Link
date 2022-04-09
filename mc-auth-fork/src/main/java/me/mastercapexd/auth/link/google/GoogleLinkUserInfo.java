package me.mastercapexd.auth.link.google;

import me.mastercapexd.auth.link.user.info.AbstractLinkUserInfo;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;
import me.mastercapexd.auth.link.user.info.confirmation.DefaultLinkUserConfirmationState;
import me.mastercapexd.auth.link.user.info.identificator.UserStringIdentificator;

public class GoogleLinkUserInfo extends AbstractLinkUserInfo {

	public GoogleLinkUserInfo(String userId, boolean confirmationEnabled) {
		super(new UserStringIdentificator(userId), new DefaultLinkUserConfirmationState(confirmationEnabled));
	}

	public GoogleLinkUserInfo(LinkUserInfo linkUserInfo) {
		super(linkUserInfo.getIdentificator(), linkUserInfo.getConfirmationState());
	}
}
