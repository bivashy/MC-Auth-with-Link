package me.mastercapexd.auth.link.telegram;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.link.user.info.LinkUserInfo;

import me.mastercapexd.auth.link.user.entry.BaseLinkEntryUser;

public class TelegramLinkEntryUser extends BaseLinkEntryUser {
    public TelegramLinkEntryUser(Account account, LinkUserInfo linkUserInfo) {
        super(TelegramLinkType.getInstance(), account, linkUserInfo);
    }

    public TelegramLinkEntryUser(Account account) {
        this(account, account.findFirstLinkUserOrNew(TelegramLinkType.LINK_USER_FILTER, TelegramLinkType.getInstance()).getLinkUserInfo());
    }
}
