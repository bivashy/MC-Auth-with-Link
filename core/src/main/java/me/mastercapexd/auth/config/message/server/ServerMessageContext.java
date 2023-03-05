package me.mastercapexd.auth.config.message.server;

import com.bivashy.auth.api.account.Account;

import me.mastercapexd.auth.config.message.context.account.BaseAccountPlaceholderContext;

public class ServerMessageContext extends BaseAccountPlaceholderContext {
    public ServerMessageContext(Account account) {
        super(account);
    }
}
