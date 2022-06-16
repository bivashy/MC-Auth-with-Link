package me.mastercapexd.auth.link.entryuser.telegram;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.entryuser.AbstractLinkEntryUser;
import me.mastercapexd.auth.link.telegram.TelegramLinkType;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;

public class TelegramLinkEntryUser extends AbstractLinkEntryUser {

    public TelegramLinkEntryUser(Account account, LinkUserInfo linkUserInfo) {
        super(TelegramLinkType.getInstance(), account, linkUserInfo);
    }

    public TelegramLinkEntryUser(Account account) {
        this(account, account.findFirstLinkUser(TelegramLinkType.LINK_USER_FILTER).get().getLinkUserInfo());
    }

}
