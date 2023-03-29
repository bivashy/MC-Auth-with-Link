package com.bivashy.auth.api.link.user.confirmation;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.type.LinkConfirmationType;

public interface LinkConfirmationUser {
    String getConfirmationCode();

    LinkConfirmationType getLinkConfirmationType();

    Account getLinkTarget();

    long getLinkTimeoutMillis();
}
