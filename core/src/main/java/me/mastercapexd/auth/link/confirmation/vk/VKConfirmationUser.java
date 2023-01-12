package me.mastercapexd.auth.link.confirmation.vk;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.confirmation.LinkConfirmationUserTemplate;
import me.mastercapexd.auth.link.confirmation.info.LinkConfirmationInfo;
import me.mastercapexd.auth.link.vk.VKLinkType;

public class VKConfirmationUser extends LinkConfirmationUserTemplate {

    public VKConfirmationUser(Account account, LinkConfirmationInfo confirmationInfo) {
        super(VKLinkType.getInstance(), account, confirmationInfo);
    }

}
