package me.mastercapexd.auth.link.user.info;

import me.mastercapexd.auth.link.user.info.confirmation.DefaultLinkUserConfirmationState;
import me.mastercapexd.auth.link.user.info.confirmation.LinkUserConfirmationState;
import me.mastercapexd.auth.link.user.info.identificator.LinkUserIdentificator;

public abstract class AbstractLinkUserInfo implements LinkUserInfo {
    protected LinkUserConfirmationState confirmationState;
    protected LinkUserIdentificator userIdentificator;

    public AbstractLinkUserInfo(LinkUserIdentificator userIdentificator, LinkUserConfirmationState confirmationState) {
        this.confirmationState = confirmationState;
        this.userIdentificator = userIdentificator;
    }

    public AbstractLinkUserInfo(LinkUserIdentificator userIdentificator) {
        this.confirmationState = new DefaultLinkUserConfirmationState();
        this.userIdentificator = userIdentificator;
    }

    @Override
    public LinkUserConfirmationState getConfirmationState() {
        return confirmationState;
    }

    @Override
    public LinkUserIdentificator getIdentificator() {
        return userIdentificator;
    }

    @Override
    public LinkUserInfo setIdentificator(LinkUserIdentificator userIdentificator) {
        this.userIdentificator = userIdentificator;
        return this;
    }

    @Override
    public LinkUserInfo setConfirmationState(LinkUserConfirmationState confirmationState) {
        this.confirmationState = confirmationState;
        return this;
    }

}
