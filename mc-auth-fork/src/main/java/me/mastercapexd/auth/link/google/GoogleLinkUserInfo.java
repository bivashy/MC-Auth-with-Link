package me.mastercapexd.auth.link.google;

import me.mastercapexd.auth.link.user.info.AbstractLinkUserInfo;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;
import me.mastercapexd.auth.link.user.info.confirmation.DefaultLinkUserConfirmationState;
import me.mastercapexd.auth.link.user.info.confirmation.LinkUserConfirmationState;
import me.mastercapexd.auth.link.user.info.identificator.UserStringIdentificator;

public class GoogleLinkUserInfo extends AbstractLinkUserInfo {

	public GoogleLinkUserInfo(String userId, boolean confirmationEnabled) {
		super(new UserStringIdentificator(userId), new DefaultLinkUserConfirmationState(confirmationEnabled));
	}

	public GoogleLinkUserInfo(String userId) {
		super(new UserStringIdentificator(userId));
	}

	public GoogleLinkUserInfo(LinkUserInfo linkUserInfo) {
		super(linkUserInfo.getIdentificator(), linkUserInfo.getConfirmationState());
	}

	@Override
	public LinkUserConfirmationState getConfirmationState() {
		return new LinkUserConfirmationState() {

			@Override
			public boolean shouldSendConfirmation() {
				return userIdentificator.asString() != null && !userIdentificator.asString().isEmpty();
			}

			@Override
			public LinkUserConfirmationState setSendConfirmation(boolean sendConfirmation) {
				throw new UnsupportedOperationException("Cannot set confirmation state for google link confirmation state");
			}
		};
	}

	@Override
	public LinkUserInfo setConfirmationState(LinkUserConfirmationState confirmationState) {
		throw new UnsupportedOperationException("Cannot set confirmation state for google link");
	}

}
