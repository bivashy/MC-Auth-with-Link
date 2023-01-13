package me.mastercapexd.auth.link.user.confirmation;

import me.mastercapexd.auth.link.user.confirmation.info.LinkConfirmationInfo;
import me.mastercapexd.auth.link.user.LinkUser;

public interface LinkConfirmationUser extends LinkUser {
    LinkConfirmationInfo getConfirmationInfo();

    long getLinkTimeoutMillis();
}
