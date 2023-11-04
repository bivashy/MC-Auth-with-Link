package me.mastercapexd.auth.link.user;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.LinkUser;
import com.bivashy.auth.api.link.user.info.LinkUserInfo;

public class LinkUserTemplate implements LinkUser {
    private final LinkType linkType;
    private final Account account;
    private final LinkUserInfo linkUserInfo;

    public LinkUserTemplate(LinkType linkType, Account account, LinkUserInfo linkUserInfo) {
        this.linkType = linkType;
        this.account = account;
        this.linkUserInfo = linkUserInfo;
    }

    @Override
    public LinkType getLinkType() {
        return linkType;
    }

    @Override
    public Account getAccount() {
        return account;
    }

    @Override
    public LinkUserInfo getLinkUserInfo() {
        return linkUserInfo;
    }

    public static LinkUser of(LinkType linkType, Account account, LinkUserInfo linkUserInfo) {
        return new LinkUserTemplate(linkType, account, linkUserInfo);
    }
}
