package me.mastercapexd.auth.link.user.info;

import com.ubivashka.function.CastableInterface;
import me.mastercapexd.auth.link.user.info.confirmation.LinkUserConfirmationState;
import me.mastercapexd.auth.link.user.info.identificator.LinkUserIdentificator;

public interface LinkUserInfo extends CastableInterface<LinkUserInfo> {
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

    /**
     * Returns modifiable confirmation states as {@link LinkUserConfirmationState}.
     *
     * @return Confirmation state of user
     */
    LinkUserConfirmationState getConfirmationState();

    /**
     * Set user confirmation state, may throw {@link UnsupportedOperationException}
     * if {@link LinkUserInfo} cannot process provided
     * {@link LinkUserConfirmationState} confirmationState.
     *
     * @param confirmationState that should be applied
     * @return this {@link LinkUserInfo}
     */
    LinkUserInfo setConfirmationState(LinkUserConfirmationState confirmationState);
}
