package me.mastercapexd.auth.database;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.link.user.info.impl.UserStringIdentificator;

import me.mastercapexd.auth.account.AuthAccountAdapter;

public class AuthAccountDatabaseProxy implements AccountDatabase {
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
    public CompletableFuture<Account> getAccountFromUUID(UUID uuid) {
        return CompletableFuture.supplyAsync(
                () -> databaseHelper.getAuthAccountDao().queryFirstAccountPlayerUUID(uuid).map(AuthAccountAdapter::new).orElse(null));
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
    public CompletableFuture<Collection<Account>> getAllLinkedAccounts(LinkType linkType) {
        return CompletableFuture.supplyAsync(
                () -> databaseHelper.getAuthAccountDao().queryAllLinkedAccounts(linkType).stream().map(AuthAccountAdapter::new).collect(Collectors.toList()));
    }

    @Override
    public CompletableFuture<Account> saveOrUpdateAccount(Account account) {
        return CompletableFuture.supplyAsync(() -> new AuthAccountAdapter(databaseHelper.getAuthAccountDao().createOrUpdateAccount(account)));
    }

    @Override
    public CompletableFuture<Void> updateAccountLinks(Account account) {
        return CompletableFuture.supplyAsync(() -> {
            databaseHelper.getAccountLinkDao().updateAccountLinks(account);
            return null;
        });
    }

    @Override
    public CompletableFuture<Void> deleteAccount(String id) {
        return CompletableFuture.supplyAsync(() -> {
            databaseHelper.getAuthAccountDao().deleteAccountById(id);
            return null;
        });
    }

    @Override
    public boolean isEnabled() {
        return databaseHelper.isEnabled();
    }
}
