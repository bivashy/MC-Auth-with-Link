package me.mastercapexd.auth.link.vk;

import me.mastercapexd.auth.link.user.info.AbstractLinkUserInfo;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;
import me.mastercapexd.auth.link.user.info.confirmation.DefaultLinkUserConfirmationState;
import me.mastercapexd.auth.link.user.info.identificator.UserNumberIdentificator;

public class VKLinkUserInfo extends AbstractLinkUserInfo {

    public VKLinkUserInfo(Integer linkUserId, boolean confirmationEnabled) {
        super(new UserNumberIdentificator(linkUserId), new DefaultLinkUserConfirmationState(confirmationEnabled));
    }

    public VKLinkUserInfo(Integer linkUserId) {
        super(new UserNumberIdentificator(linkUserId));
    }

    public VKLinkUserInfo(LinkUserInfo linkUserInfo) {
        super(linkUserInfo.getIdentificator(), linkUserInfo.getConfirmationState());
    }

}
