package com.bivashy.auth.api.premium;

import java.util.UUID;

public interface PremiumProvider {

    /**
     * This method fetches a user by their username.
     *
     * @param name The username of the user.
     * @return The user, or null if the user does not exist.
     * @throws PremiumException If the user could not be fetched.
     */
    UUID getPremiumUUIDForName(String name) throws PremiumException;
}
