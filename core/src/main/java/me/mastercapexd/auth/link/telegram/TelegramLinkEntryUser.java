package me.mastercapexd.auth.link.telegram;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.user.entry.LinkEntryUserTemplate;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;

public class TelegramLinkEntryUser extends LinkEntryUserTemplate {

    public TelegramLinkEntryUser(Account account, LinkUserInfo linkUserInfo) {
        super(TelegramLinkType.getInstance(), account, linkUserInfo);
    }

    public TelegramLinkEntryUser(Account account) {
        this(account, account.findFirstLinkUser(TelegramLinkType.LINK_USER_FILTER).get().getLinkUserInfo());
    }

}
