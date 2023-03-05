package me.mastercapexd.auth.link.telegram;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.link.user.confirmation.LinkConfirmationInfo;

import me.mastercapexd.auth.link.user.confirmation.BaseLinkConfirmationUser;

public class TelegramConfirmationUser extends BaseLinkConfirmationUser {
    public TelegramConfirmationUser(Account account, LinkConfirmationInfo confirmationInfo) {
        super(TelegramLinkType.getInstance(), account, confirmationInfo);
    }
}
