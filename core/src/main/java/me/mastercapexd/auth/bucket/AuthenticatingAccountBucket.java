package me.mastercapexd.auth.bucket;

import java.util.Collection;
import java.util.Optional;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.model.PlayerIdSupplier;

public interface AuthenticatingAccountBucket {
    Collection<String> getAccountIdEntries();

    boolean isAuthorizing(PlayerIdSupplier playerIdSupplier);

    Optional<Account> getAuthorizingAccount(PlayerIdSupplier playerIdSupplier);

    default Account getAuthorizingAccountNullable(PlayerIdSupplier playerIdSupplier) {
        return getAuthorizingAccount(playerIdSupplier).orElse(null);
    }

    Optional<Long> getEnterTimestamp(PlayerIdSupplier playerIdSupplier);

    default long getEnterTimestampOrZero(PlayerIdSupplier playerIdSupplier) {
        return getEnterTimestamp(playerIdSupplier).orElse(0L);
    }

    void addAuthorizingAccount(Account account);

    void removeAuthorizingAccount(PlayerIdSupplier playerIdSupplier);
}
