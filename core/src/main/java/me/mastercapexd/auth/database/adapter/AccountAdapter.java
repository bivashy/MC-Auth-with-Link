package me.mastercapexd.auth.database.adapter;

import com.bivashy.auth.api.account.Account;

import me.mastercapexd.auth.database.model.AuthAccount;

public class AccountAdapter extends AuthAccount {

    public AccountAdapter(Account account) {
        super(account.getDatabaseId(), account.getPlayerId(), account.getIdentifierType(), account.getCryptoProvider(), account.getLastIpAddress(),
                account.getUniqueId(), account.getName(), account.getPasswordHash()
                        .getHash(), account.getLastQuitTimestamp(), account.getLastSessionStartTimestamp(), account.isPremium());
    }

}
