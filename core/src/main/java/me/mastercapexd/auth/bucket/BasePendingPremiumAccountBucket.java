package me.mastercapexd.auth.bucket;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.bucket.PendingPremiumAccountBucket;
import com.bivashy.auth.api.bucket.PendingPremiumAccountBucket.PendingPremiumAccountState;
import com.bivashy.auth.api.event.AccountStateClearEvent;
import com.bivashy.auth.api.model.PlayerIdSupplier;

public class BasePendingPremiumAccountBucket extends BaseListBucket<PendingPremiumAccountState> implements PendingPremiumAccountBucket {

    private final AuthPlugin plugin;

    public BasePendingPremiumAccountBucket(AuthPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void addPendingPremiumAccount(Account account) {
        if (hasByValue(PendingPremiumAccountBucket.PendingPremiumAccountState::getPlayerId, account.getPlayerId()))
            return;
        modifiable().add(new BasePendingPremiumAccountState(account, System.currentTimeMillis()));
    }

    @Override
    public void removePendingPremiumAccount(PlayerIdSupplier playerIdSupplier) {
        plugin.getEventBus().post(AccountStateClearEvent.class, getPendingPremiumAccount(playerIdSupplier));
        modifiable().removeIf(accountState -> accountState.getPlayerId().equals(playerIdSupplier.getPlayerId()));
    }

    public static class BasePendingPremiumAccountState implements PendingPremiumAccountState {
        private final Account account;
        private final long enterTimestamp;

        public BasePendingPremiumAccountState(Account account, long enterTimestamp) {
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
