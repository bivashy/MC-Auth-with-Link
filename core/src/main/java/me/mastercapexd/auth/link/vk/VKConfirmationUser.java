package me.mastercapexd.auth.link.vk;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.link.user.confirmation.LinkConfirmationInfo;

import me.mastercapexd.auth.link.user.confirmation.BaseLinkConfirmationUser;

public class VKConfirmationUser extends BaseLinkConfirmationUser {
    public VKConfirmationUser(Account account, LinkConfirmationInfo confirmationInfo) {
        super(VKLinkType.getInstance(), account, confirmationInfo);
    }
}
