package me.mastercapexd.auth.link.user.confirmation;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.link.user.confirmation.info.LinkConfirmationInfo;
import me.mastercapexd.auth.link.user.LinkUserTemplate;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;

public class LinkConfirmationUserTemplate extends LinkUserTemplate implements LinkConfirmationUser {
    private final long linkTimeoutMillis;
    private final LinkConfirmationInfo confirmationInfo;

    public LinkConfirmationUserTemplate(LinkType linkType, Account account, LinkConfirmationInfo confirmationInfo) {
        super(linkType, account);
        this.linkTimeoutMillis = System.currentTimeMillis() + linkType.getSettings().getConfirmationSettings().getRemoveDelay().getMillis();
        this.confirmationInfo = confirmationInfo;
    }

    @Override
    public LinkUserInfo getLinkUserInfo() {
        return confirmationInfo;
    }

    @Override
    public LinkConfirmationInfo getConfirmationInfo() {
        return confirmationInfo;
    }

    @Override
    public long getLinkTimeoutMillis() {
        return linkTimeoutMillis;
    }
}
