package me.mastercapexd.auth.link.google;

import com.bivashy.auth.api.link.user.info.impl.UserStringIdentificator;

import me.mastercapexd.auth.link.user.BaseLinkUserInfo;

public class GoogleLinkUserInfo extends BaseLinkUserInfo {
    public GoogleLinkUserInfo(String userId, boolean confirmationEnabled) {
        super(new UserStringIdentificator(userId), confirmationEnabled);
    }

    public GoogleLinkUserInfo(String userId) {
        this(userId, userId != null && !userId.isEmpty());
    }
}
