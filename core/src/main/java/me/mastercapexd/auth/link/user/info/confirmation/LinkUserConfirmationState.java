package me.mastercapexd.auth.link.user.info.confirmation;

import me.mastercapexd.auth.function.Castable;

public interface LinkUserConfirmationState extends Castable<LinkUserConfirmationState> {
    /**
     * Resolves should confirmation message sended to the account
     *
     * @return Will be confirmation sended.
     */
    boolean shouldSendConfirmation();

    LinkUserConfirmationState setSendConfirmation(boolean sendConfirmation);
}
