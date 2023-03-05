package me.mastercapexd.auth.link.vk;

import com.bivashy.auth.api.link.user.info.impl.UserNumberIdentificator;

import me.mastercapexd.auth.link.user.BaseLinkUserInfo;

public class VKLinkUserInfo extends BaseLinkUserInfo {
    public VKLinkUserInfo(Integer linkUserId, boolean confirmationEnabled) {
        super(new UserNumberIdentificator(linkUserId), confirmationEnabled);
    }

    public VKLinkUserInfo(Integer linkUserId) {
        super(new UserNumberIdentificator(linkUserId));
    }
}
