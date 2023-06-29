package me.mastercapexd.auth.bucket;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.bucket.AuthenticatingAccountBucket;
import com.bivashy.auth.api.event.AccountStateClearEvent;
import com.bivashy.auth.api.model.PlayerIdSupplier;

public class BaseAuthenticatingAccountBucket implements AuthenticatingAccountBucket {
    private final Map<String, AuthenticatingAccountState> authenticatingAccounts = new HashMap<>();
    private final AuthPlugin plugin;

    public BaseAuthenticatingAccountBucket(AuthPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Collection<String> getAccountIdEntries() {
        return authenticatingAccounts.keySet();
    }

    @Override
    public boolean isAuthenticating(PlayerIdSupplier playerIdSupplier) {
        return authenticatingAccounts.containsKey(playerIdSupplier.getPlayerId());
    }

    @Override
    public Optional<Account> getAuthenticatingAccount(PlayerIdSupplier playerIdSupplier) {
        return Optional.ofNullable(authenticatingAccounts.getOrDefault(playerIdSupplier.getPlayerId(), null)).map(AuthenticatingAccountState::getAccount);
    }

    @Override
    public Optional<Long> getEnterTimestamp(PlayerIdSupplier playerIdSupplier) {
        return Optional.ofNullable(authenticatingAccounts.getOrDefault(playerIdSupplier.getPlayerId(), null))
                .map(AuthenticatingAccountState::getEnterTimestamp);
    }

    @Override
    public void addAuthenticatingAccount(Account account) {
        authenticatingAccounts.put(account.getPlayerId(), new AuthenticatingAccountState(account, System.currentTimeMillis()));
    }

    @Override
    public void removeAuthenticatingAccount(PlayerIdSupplier playerIdSupplier) {
        plugin.getEventBus().post(AccountStateClearEvent.class, getAuthenticatingAccount(playerIdSupplier));
        authenticatingAccounts.remove(playerIdSupplier.getPlayerId());
    }

    public static class AuthenticatingAccountState {
        private final Account account;
        private final long enterTimestamp;

        public AuthenticatingAccountState(Account account, long enterTimestamp) {
            this.account = account;
            this.enterTimestamp = enterTimestamp;
        }

        public Account getAccount() {
            return account;
        }

        public long getEnterTimestamp() {
            return enterTimestamp;
        }
    }
}
