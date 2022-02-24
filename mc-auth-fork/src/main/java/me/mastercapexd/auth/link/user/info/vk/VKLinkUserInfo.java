package me.mastercapexd.auth.link.user.info.vk;

import me.mastercapexd.auth.link.user.info.AbstractLinkUserInfo;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;

public class VKLinkUserInfo extends AbstractLinkUserInfo{

	public VKLinkUserInfo(Integer linkUserId, boolean confirmationEnabled) {
		super(linkUserId,confirmationEnabled);
	}

	public VKLinkUserInfo(LinkUserInfo linkUserInfo) {
		super(linkUserInfo.getLinkUserId(),linkUserInfo.isConfirmationEnabled());
	}
	
}
