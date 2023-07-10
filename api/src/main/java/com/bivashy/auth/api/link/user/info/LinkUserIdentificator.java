package com.bivashy.auth.api.link.user.info;

import com.bivashy.auth.api.link.user.info.impl.UserNumberIdentificator;
import com.bivashy.auth.api.link.user.info.impl.UserStringIdentificator;
import com.bivashy.auth.api.util.Castable;

public interface LinkUserIdentificator extends Castable<LinkUserIdentificator> {
    /**
     * Wraps raw id to {@link LinkUserIdentificator}. If raw id is can be parsed by {@link Long#parseLong(String)}, this method will return
     * {@link UserNumberIdentificator}, or else {@link UserStringIdentificator}.
     *
     * @param linkUserId raw user id
     * @return {@link UserNumberIdentificator} if id can be parsed by {@link Long#parseLong(String)} or else {@link UserStringIdentificator}
     */
    static LinkUserIdentificator ofParsed(String linkUserId) {
        try {
            return new UserNumberIdentificator(Long.parseLong(linkUserId));
        } catch(NumberFormatException e) {
            return new UserStringIdentificator(linkUserId);
        }
    }

    /**
     * Returns identificator of user as number, may throw
     * {@link UnsupportedOperationException} if it stores id as {@link String} or
     * more complex data
     *
     * @return Identificator as number
     */
    default long asNumber() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns identificator as string, if id stores as number, it will convert
     * number to string without throwing {@link UnsupportedOperationException},
     * because it will use {@linkplain String#valueOf}
     *
     * @return Identificator as string
     */
    default String asString() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns result of casting object to number. May throw
     * {@link NumberFormatException} if cannot format number.
     *
     * @return Is identificator can be converted to number
     */
    default boolean isNumber() {
        return false;
    }

    /**
     * Set identificator as number, may throw {@link UnsupportedOperationException}
     * if cannot set identificator as number
     *
     * @param userId New identificator
     * @return this {@link LinkUserIdentificator}
     */
    default LinkUserIdentificator setNumber(long userId) {
        throw new UnsupportedOperationException();
    }

    /**
     * Set identificator as string, may throw {@link UnsupportedOperationException}
     * if cannot set identificator as string
     *
     * @param userId New identificator
     * @return this {@link LinkUserIdentificator}
     */
    default LinkUserIdentificator setString(String userId) {
        throw new UnsupportedOperationException();
    }
}
