package me.mastercapexd.auth.link.confirmation.info;

import me.mastercapexd.auth.link.user.info.AbstractLinkUserInfo;

public abstract class AbstractLinkConfirmationInfo extends AbstractLinkUserInfo implements LinkConfirmationInfo{

	private static final String ERROR = "Cannot access in confirmation info";
	
	public AbstractLinkConfirmationInfo(Integer linkUserId) {
		super(linkUserId);
	}

	@Override
	public void setLinkUserId(Integer linkUserId) {
		throw new UnsupportedOperationException(ERROR);
	}

	@Override
	public Boolean isConfirmationEnabled() {
		throw new UnsupportedOperationException(ERROR);
	}

	@Override
	public void setConfirmationEnabled(Boolean confirmationEnabled) {
		throw new UnsupportedOperationException(ERROR);
	}

	
	
}
