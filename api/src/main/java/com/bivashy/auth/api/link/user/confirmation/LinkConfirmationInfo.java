package com.bivashy.auth.api.link.user.confirmation;

import com.bivashy.auth.api.link.user.info.LinkUserInfo;

public interface LinkConfirmationInfo extends LinkUserInfo {
    String getConfirmationCode();
}
