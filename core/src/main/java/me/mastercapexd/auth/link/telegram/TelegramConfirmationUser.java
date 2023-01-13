package me.mastercapexd.auth.link.telegram;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.user.confirmation.LinkConfirmationUserTemplate;
import me.mastercapexd.auth.link.user.confirmation.info.LinkConfirmationInfo;

public class TelegramConfirmationUser extends LinkConfirmationUserTemplate {

    public TelegramConfirmationUser(Account account, LinkConfirmationInfo confirmationInfo) {
        super(TelegramLinkType.getInstance(), account, confirmationInfo);
    }

}
