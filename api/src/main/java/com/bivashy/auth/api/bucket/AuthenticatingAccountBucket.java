package com.bivashy.auth.api.bucket;

import java.util.Collection;
import java.util.Optional;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.model.PlayerIdSupplier;

public interface AuthenticatingAccountBucket {
    Collection<String> getAccountIdEntries();

    /**
     * @deprecated Use {@link #isAuthenticating(PlayerIdSupplier)}
     */
    @Deprecated
    default boolean isAuthorizing(PlayerIdSupplier playerIdSupplier) {
        return isAuthenticating(playerIdSupplier);
    }

    /**
     * @deprecated Use {@link #getAuthenticatingAccount(PlayerIdSupplier)}
     */
    @Deprecated
    default Optional<Account> getAuthorizingAccount(PlayerIdSupplier playerIdSupplier) {
        return getAuthenticatingAccount(playerIdSupplier);
    }

    /**
     * @deprecated Use {@link #getAuthenticatingAccount(PlayerIdSupplier)}
     */
    @Deprecated
    default Account getAuthorizingAccountNullable(PlayerIdSupplier playerIdSupplier) {
        return getAuthenticatingAccountNullable(playerIdSupplier);
    }

    boolean isAuthenticating(PlayerIdSupplier playerIdSupplier);

    Optional<Account> getAuthenticatingAccount(PlayerIdSupplier playerIdSupplier);

    default Account getAuthenticatingAccountNullable(PlayerIdSupplier playerIdSupplier) {
        return getAuthenticatingAccount(playerIdSupplier).orElse(null);
    }

    Optional<Long> getEnterTimestamp(PlayerIdSupplier playerIdSupplier);

    default long getEnterTimestampOrZero(PlayerIdSupplier playerIdSupplier) {
        return getEnterTimestamp(playerIdSupplier).orElse(0L);
    }

    /**
     * @deprecated Use {@link #addAuthenticatingAccount(Account)}
     */
    @Deprecated
    default void addAuthorizingAccount(Account account) {
        addAuthenticatingAccount(account);
    }

    /**
     * @deprecated Use {@link #removeAuthenticatingAccount(PlayerIdSupplier)}
     */
    @Deprecated
    default void removeAuthorizingAccount(PlayerIdSupplier playerIdSupplier) {
        removeAuthenticatingAccount(playerIdSupplier);
    }

    void addAuthenticatingAccount(Account account);

    void removeAuthenticatingAccount(PlayerIdSupplier playerIdSupplier);
}
