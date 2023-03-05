package me.mastercapexd.auth.link.google;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.link.user.info.LinkUserInfo;

import me.mastercapexd.auth.link.user.entry.BaseLinkEntryUser;

public class GoogleLinkEntryUser extends BaseLinkEntryUser {
    public GoogleLinkEntryUser(Account account, LinkUserInfo linkUserInfo) {
        super(GoogleLinkType.getInstance(), account, linkUserInfo);
    }

    public GoogleLinkEntryUser(Account account) {
        this(account, account.findFirstLinkUserOrNew(GoogleLinkType.LINK_USER_FILTER, GoogleLinkType.getInstance()).getLinkUserInfo());
    }
}
