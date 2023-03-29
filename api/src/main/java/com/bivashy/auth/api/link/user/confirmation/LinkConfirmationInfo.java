package com.bivashy.auth.api.link.user.confirmation;

import com.bivashy.auth.api.type.LinkConfirmationType;

public interface LinkConfirmationInfo {
    String getConfirmationCode();

    LinkConfirmationType getLinkConfirmationType();
}
