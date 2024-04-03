package com.bivashy.auth.api.bucket;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.bucket.PendingPremiumAccountBucket.PendingPremiumAccountState;
import com.bivashy.auth.api.model.PlayerIdSupplier;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public interface PendingPremiumAccountBucket extends Bucket<PendingPremiumAccountState> {
    default Collection<String> getAccountIdEntries() {
        return stream().map(PendingPremiumAccountState::getAccount).map(PlayerIdSupplier::getPlayerId).collect(Collectors.toList());
    }

    default boolean isPendingPremium(PlayerIdSupplier playerIdSupplier) {
        return hasByValue(PendingPremiumAccountState::getPlayerId, playerIdSupplier.getPlayerId());
    }

    default Optional<Account> getPendingPremiumAccountByName(String username) {
        return findFirstByValue(PendingPremiumAccountState::getPlayerId, username).map(PendingPremiumAccountState::getAccount);
    }

    default Optional<Account> getPendingPremiumAccount(PlayerIdSupplier playerIdSupplier) {
        return findFirstByValue(PendingPremiumAccountState::getPlayerId, playerIdSupplier.getPlayerId()).map(PendingPremiumAccountState::getAccount);
    }

    default Account getPendingPremiumAccountNullable(PlayerIdSupplier playerIdSupplier) {
        return getPendingPremiumAccount(playerIdSupplier).orElse(null);
    }

    default Optional<Long> getEnterTimestamp(PlayerIdSupplier playerIdSupplier) {
        return findFirstByValue(PendingPremiumAccountState::getPlayerId, playerIdSupplier.getPlayerId()).map(PendingPremiumAccountState::getEnterTimestamp);
    }

    default long getEnterTimestampOrZero(PlayerIdSupplier playerIdSupplier) {
        return getEnterTimestamp(playerIdSupplier).orElse(0L);
    }

    void addPendingPremiumAccount(Account account);

    void removePendingPremiumAccount(PlayerIdSupplier playerIdSupplier);


    interface PendingPremiumAccountState {
        default String getPlayerId() {
            return getAccount().getPlayerId();
        }

        Account getAccount();

        long getEnterTimestamp();
    }
}
