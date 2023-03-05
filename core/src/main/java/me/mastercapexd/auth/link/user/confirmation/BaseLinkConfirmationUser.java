package me.mastercapexd.auth.link.user.confirmation;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.confirmation.LinkConfirmationInfo;
import com.bivashy.auth.api.link.user.confirmation.LinkConfirmationUser;
import com.bivashy.auth.api.link.user.info.LinkUserInfo;

import me.mastercapexd.auth.link.user.LinkUserTemplate;

public class BaseLinkConfirmationUser extends LinkUserTemplate implements LinkConfirmationUser {
    private final long linkTimeoutMillis;
    private final LinkConfirmationInfo confirmationInfo;

    public BaseLinkConfirmationUser(LinkType linkType, Account account, LinkConfirmationInfo confirmationInfo) {
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
