package me.mastercapexd.auth.server.commands.parameters;

import java.util.concurrent.CompletableFuture;

import com.bivashy.auth.api.account.Account;

public class ArgumentAccount {
    private final CompletableFuture<Account> account;

    public ArgumentAccount(CompletableFuture<Account> account) {
        this.account = account;
    }

    public CompletableFuture<Account> future() {
        return account;
    }
}
