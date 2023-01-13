package me.mastercapexd.auth.link.vk;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.user.confirmation.LinkConfirmationUserTemplate;
import me.mastercapexd.auth.link.user.confirmation.info.LinkConfirmationInfo;

public class VKConfirmationUser extends LinkConfirmationUserTemplate {

    public VKConfirmationUser(Account account, LinkConfirmationInfo confirmationInfo) {
        super(VKLinkType.getInstance(), account, confirmationInfo);
    }

}
