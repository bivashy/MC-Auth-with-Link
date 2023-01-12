package me.mastercapexd.auth.link.confirmation.telegram;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.confirmation.LinkConfirmationUserTemplate;
import me.mastercapexd.auth.link.confirmation.info.LinkConfirmationInfo;
import me.mastercapexd.auth.link.telegram.TelegramLinkType;

public class TelegramConfirmationUser extends LinkConfirmationUserTemplate {

    public TelegramConfirmationUser(Account account, LinkConfirmationInfo confirmationInfo) {
        super(TelegramLinkType.getInstance(), account, confirmationInfo);
    }

}
