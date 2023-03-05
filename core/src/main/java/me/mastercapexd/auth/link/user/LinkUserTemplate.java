package me.mastercapexd.auth.link.user;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.LinkUser;
import com.bivashy.auth.api.link.user.info.LinkUserInfo;

public abstract class LinkUserTemplate implements LinkUser {
    protected final LinkType linkType;
    protected final Account account;

    public LinkUserTemplate(LinkType linkType, Account account) {
        this.linkType = linkType;
        this.account = account;
    }

    @Override
    public LinkType getLinkType() {
        return linkType;
    }

    @Override
    public Account getAccount() {
        return account;
    }

    public static LinkUser of(LinkType linkType, Account account, LinkUserInfo linkUserInfo) {
        return new LinkUserTemplate(linkType, account) {
            @Override
            public LinkUserInfo getLinkUserInfo() {
                return linkUserInfo;
            }
        };
    }
}
