package com.bivashy.auth.api.bucket;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.bucket.AuthenticatingAccountBucket.AuthenticatingAccountState;
import com.bivashy.auth.api.model.PlayerIdSupplier;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public interface PendingLoginBucket extends Bucket<PendingLoginBucket.PendingLoginState> {
    default boolean hasFailedLogin(String ip, String username) {
        return hasByValue(PendingLoginState::getPendingLoginId, ip + username);
    }

    void addPendingLogin(String ip, String username);

    void removePendingLogin(String ip, String username);

    interface PendingLoginState {
        default String getPendingLoginId() {
            return getAddress() + getUsername();
        }

        String getAddress();
        String getUsername();
    }
}
