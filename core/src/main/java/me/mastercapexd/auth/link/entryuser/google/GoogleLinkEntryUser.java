package me.mastercapexd.auth.link.entryuser.google;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.entryuser.AbstractLinkEntryUser;
import me.mastercapexd.auth.link.google.GoogleLinkType;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;

public class GoogleLinkEntryUser extends AbstractLinkEntryUser {

    public GoogleLinkEntryUser(Account account, LinkUserInfo linkUserInfo) {
        super(GoogleLinkType.getInstance(), account, linkUserInfo);
    }

    public GoogleLinkEntryUser(Account account) {
        this(account, account.findFirstLinkUser(GoogleLinkType.LINK_USER_FILTER).get().getLinkUserInfo());
    }

}
