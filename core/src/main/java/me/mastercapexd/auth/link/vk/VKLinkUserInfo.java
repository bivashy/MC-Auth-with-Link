package me.mastercapexd.auth.link.vk;

import me.mastercapexd.auth.link.user.info.LinkUserInfoTemplate;
import me.mastercapexd.auth.link.user.info.identificator.UserNumberIdentificator;

public class VKLinkUserInfo extends LinkUserInfoTemplate {
    public VKLinkUserInfo(Integer linkUserId, boolean confirmationEnabled) {
        super(new UserNumberIdentificator(linkUserId), confirmationEnabled);
    }

    public VKLinkUserInfo(Integer linkUserId) {
        super(new UserNumberIdentificator(linkUserId));
    }
}
