package com.bivashy.auth.api.bucket;

import java.util.Collection;
import java.util.Optional;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.model.PlayerIdSupplier;

public interface AuthenticatingAccountBucket {
    Collection<String> getAccountIdEntries();

    @Deprecated
    boolean isAuthorizing(PlayerIdSupplier playerIdSupplier);

    @Deprecated
    Optional<Account> getAuthorizingAccount(PlayerIdSupplier playerIdSupplier);

    @Deprecated
    default Account getAuthorizingAccountNullable(PlayerIdSupplier playerIdSupplier) {
        return getAuthorizingAccount(playerIdSupplier).orElse(null);
    }

    Optional<Long> getEnterTimestamp(PlayerIdSupplier playerIdSupplier);

    default long getEnterTimestampOrZero(PlayerIdSupplier playerIdSupplier) {
        return getEnterTimestamp(playerIdSupplier).orElse(0L);
    }

    @Deprecated
    void addAuthorizingAccount(Account account);

    @Deprecated
    void removeAuthorizingAccount(PlayerIdSupplier playerIdSupplier);
}
