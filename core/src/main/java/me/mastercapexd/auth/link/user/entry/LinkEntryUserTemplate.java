package me.mastercapexd.auth.link.user.entry;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.link.user.LinkUserTemplate;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;

public class LinkEntryUserTemplate extends LinkUserTemplate implements LinkEntryUser {
    protected final long confirmationStartTime = System.currentTimeMillis();
    protected final LinkUserInfo linkUserInfo;
    protected boolean confirmed = false;

    public LinkEntryUserTemplate(LinkType linkType, Account account, LinkUserInfo linkUserInfo) {
        super(linkType, account);
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
