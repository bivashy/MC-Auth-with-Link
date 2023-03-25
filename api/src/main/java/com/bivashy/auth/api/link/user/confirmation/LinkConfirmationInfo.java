package com.bivashy.auth.api.link.user.confirmation;

import com.bivashy.auth.api.link.user.info.LinkUserInfo;
import com.bivashy.auth.api.type.LinkConfirmationType;

public interface LinkConfirmationInfo extends LinkUserInfo {
    String getConfirmationCode();

    LinkConfirmationType getLinkConfirmationType();
}
