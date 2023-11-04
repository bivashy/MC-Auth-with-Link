package me.mastercapexd.auth.link.user.entry;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.entry.LinkEntryUser;
import com.bivashy.auth.api.link.user.info.LinkUserInfo;

import me.mastercapexd.auth.link.user.LinkUserTemplate;

public class BaseLinkEntryUser extends LinkUserTemplate implements LinkEntryUser {
    protected final long confirmationStartTime = System.currentTimeMillis();
    protected final LinkUserInfo linkUserInfo;
    protected boolean confirmed = false;

    public BaseLinkEntryUser(LinkType linkType, Account account, LinkUserInfo linkUserInfo) {
        super(linkType, account, linkUserInfo);
        this.linkUserInfo = linkUserInfo;
    }

    public long getConfirmationStartTime() {
        return confirmationStartTime;
    }

    @Override
    public LinkUserInfo getLinkUserInfo() {
        return linkUserInfo;
    }

    @Override
    public boolean isConfirmed() {
        return confirmed;
    }

    @Override
    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
