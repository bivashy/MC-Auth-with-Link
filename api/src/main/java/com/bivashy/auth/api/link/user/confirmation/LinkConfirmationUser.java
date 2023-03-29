package com.bivashy.auth.api.link.user.confirmation;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.type.LinkConfirmationType;

public interface LinkConfirmationUser {
    String getConfirmationCode();

    LinkConfirmationType getLinkConfirmationType();

    Account getLinkTarget();

    long getLinkTimeoutTimestamp();

    LinkType getLinkType();

    LinkUserIdentificator getLinkUserIdentificator();

    void setLinkUserIdentificator(LinkUserIdentificator linkUserIdentificator);
}
