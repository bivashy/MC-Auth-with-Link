package com.bivashy.auth.api.link.user.info;

import com.bivashy.auth.api.link.user.info.impl.BaseLinkUserInfo;
import com.bivashy.auth.api.util.Castable;

public interface LinkUserInfo extends Castable<LinkUserInfo> {
    static LinkUserInfo of(LinkUserIdentificator userId) {
        return new BaseLinkUserInfo(userId);
    }

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
