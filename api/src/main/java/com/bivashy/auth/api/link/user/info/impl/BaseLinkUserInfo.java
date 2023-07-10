package com.bivashy.auth.api.link.user.info.impl;

import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.link.user.info.LinkUserInfo;

public class BaseLinkUserInfo implements LinkUserInfo {
    private LinkUserIdentificator identificator;
    private boolean confirmationEnabled = true;

    public BaseLinkUserInfo(LinkUserIdentificator identificator, boolean confirmationEnabled) {
        this.identificator = identificator;
        this.confirmationEnabled = confirmationEnabled;
    }

    public BaseLinkUserInfo(LinkUserIdentificator identificator) {
        this.identificator = identificator;
    }

    @Override
    public LinkUserIdentificator getIdentificator() {
        return identificator;
    }

    @Override
    public LinkUserInfo setIdentificator(LinkUserIdentificator identificator) {
        this.identificator = identificator;
        return this;
    }

    @Override
    public boolean isConfirmationEnabled() {
        return confirmationEnabled;
    }

    @Override
    public LinkUserInfo setConfirmationEnabled(boolean confirmationEnabled) {
        this.confirmationEnabled = confirmationEnabled;
        return this;
    }
}
