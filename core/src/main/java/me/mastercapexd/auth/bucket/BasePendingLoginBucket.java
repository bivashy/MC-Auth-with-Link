package me.mastercapexd.auth.bucket;

import com.bivashy.auth.api.bucket.PendingLoginBucket;

public class BasePendingLoginBucket extends BaseListBucket<PendingLoginBucket.PendingLoginState> implements PendingLoginBucket {
    @Override
    public void addPendingLogin(String ip, String username) {
        if (hasByValue(PendingLoginState::getPendingLoginId, ip + username))
            return;
        modifiable().add(new BasePendingLoginState(ip, username));
    }

    @Override
    public void removePendingLogin(String ip, String username) {
        modifiable().removeIf(pendingLoginState -> pendingLoginState.getPendingLoginId().equals(ip + username));
    }

    public static class BasePendingLoginState implements PendingLoginState {
        private final String ip;
        private final String username;

        public BasePendingLoginState(String ip, String username) {
            this.ip = ip;
            this.username = username;
        }

        @Override
        public String getAddress() {
            return ip;
        }

        @Override
        public String getUsername() {
            return username;
        }
    }

}
