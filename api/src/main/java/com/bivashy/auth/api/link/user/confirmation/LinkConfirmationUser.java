package com.bivashy.auth.api.link.user.confirmation;

public interface LinkConfirmationUser {
    LinkConfirmationInfo getConfirmationInfo();

    long getLinkTimeoutMillis();
}
