package me.mastercapexd.auth.link.telegram;

import me.mastercapexd.auth.link.user.info.AbstractLinkUserInfo;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;
import me.mastercapexd.auth.link.user.info.confirmation.DefaultLinkUserConfirmationState;
import me.mastercapexd.auth.link.user.info.identificator.UserNumberIdentificator;

public class TelegramLinkUserInfo extends AbstractLinkUserInfo {

	public TelegramLinkUserInfo(Long linkUserId, boolean confirmationEnabled) {
		super(new UserNumberIdentificator(linkUserId), new DefaultLinkUserConfirmationState(confirmationEnabled));
	}

	public TelegramLinkUserInfo(Long linkUserId) {
		super(new UserNumberIdentificator(linkUserId));
	}

	public TelegramLinkUserInfo(LinkUserInfo linkUserInfo) {
		super(linkUserInfo.getIdentificator(), linkUserInfo.getConfirmationState());
	}

}
