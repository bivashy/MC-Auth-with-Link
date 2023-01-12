package me.mastercapexd.auth.link.telegram;

import me.mastercapexd.auth.link.user.info.LinkUserInfoTemplate;
import me.mastercapexd.auth.link.user.info.identificator.UserNumberIdentificator;

public class TelegramLinkUserInfo extends LinkUserInfoTemplate {
    public TelegramLinkUserInfo(Long linkUserId, boolean confirmationEnabled) {
        super(new UserNumberIdentificator(linkUserId), confirmationEnabled);
    }

    public TelegramLinkUserInfo(Long linkUserId) {
        super(new UserNumberIdentificator(linkUserId));
    }
}
