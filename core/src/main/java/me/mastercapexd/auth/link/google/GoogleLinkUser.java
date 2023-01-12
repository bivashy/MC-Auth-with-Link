package me.mastercapexd.auth.link.google;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.user.LinkUserTemplate;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;

public class GoogleLinkUser extends LinkUserTemplate {
    private GoogleLinkUserInfo linkInfoAccount;

    public GoogleLinkUser(Account account, String googleKey) {
        super(GoogleLinkType.getInstance(), account);
        this.linkInfoAccount = new GoogleLinkUserInfo(googleKey);
    }

    @Override
    public LinkUserInfo getLinkUserInfo() {
        return linkInfoAccount;
    }
}
