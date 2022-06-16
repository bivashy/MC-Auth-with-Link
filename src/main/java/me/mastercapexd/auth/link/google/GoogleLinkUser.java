package me.mastercapexd.auth.link.google;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.user.AbstractLinkUser;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;

public class GoogleLinkUser extends AbstractLinkUser {
    private GoogleLinkUserInfo linkInfoAccount;

    public GoogleLinkUser(Account account, String googleKey, boolean confirmationEnabled) {
        super(GoogleLinkType.getInstance(), account);
        this.linkInfoAccount = new GoogleLinkUserInfo(googleKey, confirmationEnabled);
    }

    public GoogleLinkUser(Account account, String googleKey) {
        super(GoogleLinkType.getInstance(), account);
        this.linkInfoAccount = new GoogleLinkUserInfo(googleKey);
    }

    @Override
    public LinkUserInfo getLinkUserInfo() {
        return linkInfoAccount;
    }
}
