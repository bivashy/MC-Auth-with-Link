package me.mastercapexd.auth.storage;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.account.AuthAccountAdapter;
import me.mastercapexd.auth.link.user.info.identificator.LinkUserIdentificator;
import me.mastercapexd.auth.link.user.info.identificator.UserStringIdentificator;

public class AuthAccountDatabaseProxy implements AccountStorage {
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
