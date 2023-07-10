package me.mastercapexd.auth.link.telegram;

import com.bivashy.auth.api.link.user.info.impl.BaseLinkUserInfo;
import com.bivashy.auth.api.link.user.info.impl.UserNumberIdentificator;

public class TelegramLinkUserInfo extends BaseLinkUserInfo {
    public TelegramLinkUserInfo(Long linkUserId, boolean confirmationEnabled) {
        super(new UserNumberIdentificator(linkUserId), confirmationEnabled);
    }

    public TelegramLinkUserInfo(Long linkUserId) {
        super(new UserNumberIdentificator(linkUserId));
    }
}
