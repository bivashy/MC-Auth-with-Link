package me.mastercapexd.auth.messenger.commands.parameters;

import me.mastercapexd.auth.link.confirmation.LinkConfirmationUser;

public class MessengerLinkContext {
    private final LinkConfirmationUser confirmationUser;
    private final String linkCode;

    public MessengerLinkContext(String linkCode, LinkConfirmationUser confirmationUser) {
        this.confirmationUser = confirmationUser;
        this.linkCode = linkCode;
    }

    public String getLinkCode() {
        return linkCode;
    }

    public LinkConfirmationUser getConfirmationUser() {
        return confirmationUser;
    }
}
