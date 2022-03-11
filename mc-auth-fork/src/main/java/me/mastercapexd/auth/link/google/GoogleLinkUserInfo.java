package me.mastercapexd.auth.link.google;

import me.mastercapexd.auth.link.user.info.AbstractLinkUserInfo;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;

public class GoogleLinkUserInfo extends AbstractLinkUserInfo{

	private final String userId;
	
	public GoogleLinkUserInfo(String userId, boolean confirmationEnabled) {
		super(-1,confirmationEnabled);
		this.userId = userId;
	}

	public GoogleLinkUserInfo(LinkUserInfo linkUserInfo,String userId) {
		super(linkUserInfo.getLinkUserId(),linkUserInfo.isConfirmationEnabled());
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}
}
