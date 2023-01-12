package me.mastercapexd.auth.link.user.info;

import me.mastercapexd.auth.function.Castable;
import me.mastercapexd.auth.link.user.info.identificator.LinkUserIdentificator;

public interface LinkUserInfo extends Castable<LinkUserInfo> {
    /**
     * Returns used identificator as {@link LinkUserIdentificator}. It uses
     * interface because identificator can be anything (string, integer, UUID,
     * link).
     *
     * @return Identificator of user.
     */
    LinkUserIdentificator getIdentificator();

    /**
     * Set user identificator, may throw {@link UnsupportedOperationException} if
     * {@link LinkUserInfo} cannot process provided {@link LinkUserIdentificator}
     * identificator.
     *
     * @param userIdentificator that should be applied
     * @return this {@link LinkUserInfo}
     */
    LinkUserInfo setIdentificator(LinkUserIdentificator userIdentificator);

    boolean isConfirmationEnabled();

    LinkUserInfo setConfirmationEnabled(boolean confirmationEnabled);
}
