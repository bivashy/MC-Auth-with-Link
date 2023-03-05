package me.mastercapexd.auth.link.user;

import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.link.user.info.LinkUserInfo;

public class BaseLinkUserInfo implements LinkUserInfo {
    protected LinkUserIdentificator userIdentificator;
    protected boolean confirmationEnabled;

    public BaseLinkUserInfo(LinkUserIdentificator userIdentificator, boolean confirmationEnabled) {
        this.userIdentificator = userIdentificator;
        this.confirmationEnabled = confirmationEnabled;
    }

    public BaseLinkUserInfo(LinkUserIdentificator userIdentificator) {
        this(userIdentificator, true);
    }

    @Override
    public LinkUserIdentificator getIdentificator() {
        return userIdentificator;
    }

    @Override
    public LinkUserInfo setIdentificator(LinkUserIdentificator userIdentificator) {
        this.userIdentificator = userIdentificator;
        return this;
    }

    @Override
    public boolean isConfirmationEnabled() {
        return confirmationEnabled;
    }

    @Override
    public LinkUserInfo setConfirmationEnabled(boolean confirmationEnabled) {
        this.confirmationEnabled = confirmationEnabled;
        return this;
    }
}
