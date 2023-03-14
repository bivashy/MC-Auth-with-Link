package me.mastercapexd.auth.database;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.link.user.info.impl.UserStringIdentificator;

import me.mastercapexd.auth.account.AuthAccountAdapter;

public class AuthAccountDatabaseProxy implements AccountDatabase {
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    private final DatabaseHelper databaseHelper;

    public AuthAccountDatabaseProxy(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public CompletableFuture<Account> getAccount(String id) {
        return CompletableFuture.supplyAsync(() -> databaseHelper.getAuthAccountDao().queryFirstAccountPlayerId(id).map(AuthAccountAdapter::new).orElse(null));
    }

    @Override
    public CompletableFuture<Account> getAccountFromName(String playerName) {
        return CompletableFuture.supplyAsync(
                () -> databaseHelper.getAuthAccountDao().queryFirstAccountPlayerName(playerName).map(AuthAccountAdapter::new).orElse(null));
    }

    @Override
    public CompletableFuture<Collection<Account>> getAccountsByVKID(Integer id) {
        return getAccountsFromLinkIdentificator(new UserStringIdentificator(Integer.toString(id)));
    }

    @Override
    public CompletableFuture<Collection<Account>> getAccountsFromLinkIdentificator(LinkUserIdentificator identificator) {
        return CompletableFuture.supplyAsync(() -> databaseHelper.getAuthAccountDao()
                .queryAccounts(identificator)
                .stream()
                .map(AuthAccountAdapter::new)
                .collect(Collectors.toList()));
    }

    @Override
    public CompletableFuture<Collection<Account>> getAllAccounts() {
        return CompletableFuture.supplyAsync(
                () -> databaseHelper.getAuthAccountDao().queryAllAccounts().stream().map(AuthAccountAdapter::new).collect(Collectors.toList()));
    }

    @Override
    public CompletableFuture<Collection<Account>> getAllLinkedAccounts() {
        return CompletableFuture.supplyAsync(
                () -> databaseHelper.getAuthAccountDao().queryAllLinkedAccounts().stream().map(AuthAccountAdapter::new).collect(Collectors.toList()));
    }

    @Override
    public void saveOrUpdateAccount(Account account) {
        EXECUTOR_SERVICE.execute(() -> databaseHelper.getAuthAccountDao().createOrUpdateAccount(account));
    }

    @Override
    public void deleteAccount(String id) {
        EXECUTOR_SERVICE.execute(() -> databaseHelper.getAuthAccountDao().deleteAccountById(id));
    }
}
