package me.mastercapexd.auth.link.user.confirmation.info;

import me.mastercapexd.auth.link.user.info.LinkUserInfo;

public interface LinkConfirmationInfo extends LinkUserInfo {
    String getConfirmationCode();
}
