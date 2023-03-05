package me.mastercapexd.auth.link.google;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.link.user.info.LinkUserInfo;

import me.mastercapexd.auth.link.user.LinkUserTemplate;

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
