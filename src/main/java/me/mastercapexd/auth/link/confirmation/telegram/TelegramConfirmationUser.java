package me.mastercapexd.auth.link.confirmation.telegram;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.confirmation.AbstractLinkConfirmationUser;
import me.mastercapexd.auth.link.confirmation.info.LinkConfirmationInfo;
import me.mastercapexd.auth.link.telegram.TelegramLinkType;

public class TelegramConfirmationUser extends AbstractLinkConfirmationUser {

    public TelegramConfirmationUser(Account account, LinkConfirmationInfo confirmationInfo) {
        super(TelegramLinkType.getInstance(), account, confirmationInfo);
    }

}
