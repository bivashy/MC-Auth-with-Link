package com.bivashy.auth.api.bucket;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.bucket.AuthenticatingAccountBucket.AuthenticatingAccountState;
import com.bivashy.auth.api.model.PlayerIdSupplier;

public interface AuthenticatingAccountBucket extends Bucket<AuthenticatingAccountState> {
    default Collection<String> getAccountIdEntries() {
        return stream().map(AuthenticatingAccountState::getAccount).map(PlayerIdSupplier::getPlayerId).collect(Collectors.toList());
    }

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

    default boolean isAuthenticating(PlayerIdSupplier playerIdSupplier) {
        return hasByValue(AuthenticatingAccountState::getPlayerId, playerIdSupplier.getPlayerId());
    }

    default Optional<Account> getAuthenticatingAccount(PlayerIdSupplier playerIdSupplier) {
        return findFirstByValue(AuthenticatingAccountState::getPlayerId, playerIdSupplier.getPlayerId()).map(AuthenticatingAccountState::getAccount);
    }

    default Account getAuthenticatingAccountNullable(PlayerIdSupplier playerIdSupplier) {
        return getAuthenticatingAccount(playerIdSupplier).orElse(null);
    }

    default Optional<Long> getEnterTimestamp(PlayerIdSupplier playerIdSupplier) {
        return findFirstByValue(AuthenticatingAccountState::getPlayerId, playerIdSupplier.getPlayerId()).map(AuthenticatingAccountState::getEnterTimestamp);
    }

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

    interface AuthenticatingAccountState {
        default String getPlayerId() {
            return getAccount().getPlayerId();
        }

        Account getAccount();

        long getEnterTimestamp();


    }
}
