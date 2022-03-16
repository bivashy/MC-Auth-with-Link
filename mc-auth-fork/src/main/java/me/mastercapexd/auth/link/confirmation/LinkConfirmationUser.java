package me.mastercapexd.auth.link.confirmation;

import me.mastercapexd.auth.link.confirmation.info.LinkConfirmationInfo;
import me.mastercapexd.auth.link.user.LinkUser;

public interface LinkConfirmationUser extends LinkUser {
	LinkConfirmationInfo getConfirmationInfo();
	
	Long getLinkTimeoutMillis();
}
