package me.mastercapexd.auth.bucket;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.bucket.AuthenticatingAccountBucket;
import com.bivashy.auth.api.bucket.AuthenticatingAccountBucket.AuthenticatingAccountState;
import com.bivashy.auth.api.event.AccountStateClearEvent;
import com.bivashy.auth.api.model.PlayerIdSupplier;

public class BaseAuthenticatingAccountBucket extends BaseListBucket<AuthenticatingAccountState> implements AuthenticatingAccountBucket {

    private final AuthPlugin plugin;

    public BaseAuthenticatingAccountBucket(AuthPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void addAuthenticatingAccount(Account account) {
        if (hasByValue(AuthenticatingAccountState::getPlayerId, account.getPlayerId()))
            return;
        modifiable().add(new BaseAuthenticatingAccountState(account, System.currentTimeMillis()));
    }

    @Override
    public void removeAuthenticatingAccount(PlayerIdSupplier playerIdSupplier) {
        plugin.getEventBus().post(AccountStateClearEvent.class, getAuthenticatingAccount(playerIdSupplier));
        modifiable().removeIf(accountState -> accountState.getPlayerId().equals(playerIdSupplier.getPlayerId()));
    }

    public static class BaseAuthenticatingAccountState implements AuthenticatingAccountState {

        private final Account account;
        private final long enterTimestamp;

        public BaseAuthenticatingAccountState(Account account, long enterTimestamp) {
            this.account = account;
            this.enterTimestamp = enterTimestamp;
        }

        @Override
        public Account getAccount() {
            return account;
        }

        @Override
        public long getEnterTimestamp() {
            return enterTimestamp;
        }

    }

}
