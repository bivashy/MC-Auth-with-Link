package com.bivashy.auth.api.link.user.confirmation;

import com.bivashy.auth.api.link.user.LinkUser;

public interface LinkConfirmationUser extends LinkUser {
    LinkConfirmationInfo getConfirmationInfo();

    long getLinkTimeoutMillis();
}
