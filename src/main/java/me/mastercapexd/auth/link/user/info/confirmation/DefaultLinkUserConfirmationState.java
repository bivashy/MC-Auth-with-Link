package me.mastercapexd.auth.link.user.info.confirmation;

public class DefaultLinkUserConfirmationState implements LinkUserConfirmationState {
    private boolean sendConfirmation = true;

    public DefaultLinkUserConfirmationState(boolean sendConfirmation) {
        this.sendConfirmation = sendConfirmation;
    }

    public DefaultLinkUserConfirmationState() {
    }

    @Override
    public boolean shouldSendConfirmation() {
        return sendConfirmation;
    }

    @Override
    public LinkUserConfirmationState setSendConfirmation(boolean sendConfirmation) {
        this.sendConfirmation = sendConfirmation;
        return this;
    }
}
