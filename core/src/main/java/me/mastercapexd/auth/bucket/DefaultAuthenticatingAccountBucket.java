package me.mastercapexd.auth.bucket;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.event.AccountStateClearEvent;
import me.mastercapexd.auth.model.PlayerIdSupplier;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class DefaultAuthenticatingAccountBucket implements AuthenticatingAccountBucket {
    private final Map<String, AuthenticatingAccountState> authenticatingAccounts = new HashMap<>();
    private final ProxyPlugin plugin;

    public DefaultAuthenticatingAccountBucket(ProxyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Collection<String> getAccountIdEntries() {
        return authenticatingAccounts.keySet();
    }

    @Override
    public boolean isAuthorizing(PlayerIdSupplier playerIdSupplier) {
        return authenticatingAccounts.containsKey(playerIdSupplier.getPlayerId());
    }

    @Override
    public Optional<Account> getAuthorizingAccount(PlayerIdSupplier playerIdSupplier) {
        return Optional.ofNullable(authenticatingAccounts.getOrDefault(playerIdSupplier.getPlayerId(), null)).map(AuthenticatingAccountState::getAccount);
    }

    @Override
    public Optional<Long> getEnterTimestamp(PlayerIdSupplier playerIdSupplier) {
        return Optional.ofNullable(authenticatingAccounts.getOrDefault(playerIdSupplier.getPlayerId(), null))
                .map(AuthenticatingAccountState::getEnterTimestamp);
    }

    @Override
    public void addAuthorizingAccount(Account account) {
        authenticatingAccounts.put(account.getPlayerId(), new AuthenticatingAccountState(account, System.currentTimeMillis()));
    }

    @Override
    public void removeAuthorizingAccount(PlayerIdSupplier playerIdSupplier) {
        plugin.getEventBus().post(AccountStateClearEvent.class, getAuthorizingAccount(playerIdSupplier));
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
