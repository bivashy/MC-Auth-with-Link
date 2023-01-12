package me.mastercapexd.auth.link.google;

import me.mastercapexd.auth.link.user.info.LinkUserInfoTemplate;
import me.mastercapexd.auth.link.user.info.identificator.UserStringIdentificator;

public class GoogleLinkUserInfo extends LinkUserInfoTemplate {
    public GoogleLinkUserInfo(String userId, boolean confirmationEnabled) {
        super(new UserStringIdentificator(userId), confirmationEnabled);
    }

    public GoogleLinkUserInfo(String userId) {
        this(userId, userId != null && !userId.isEmpty());
    }
}
