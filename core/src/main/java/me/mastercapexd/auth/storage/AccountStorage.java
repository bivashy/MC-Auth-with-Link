package me.mastercapexd.auth.storage;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.user.info.identificator.LinkUserIdentificator;

public interface AccountStorage {
    CompletableFuture<Account> getAccount(String id);

    CompletableFuture<Account> getAccountFromName(String playerName);

    @Deprecated
    CompletableFuture<Collection<Account>> getAccountsByVKID(Integer id);

    CompletableFuture<Collection<Account>> getAccountsFromLinkIdentificator(LinkUserIdentificator identificator);

    CompletableFuture<Collection<Account>> getAllAccounts();

    CompletableFuture<Collection<Account>> getAllLinkedAccounts();

    void saveOrUpdateAccount(Account account);

    void deleteAccount(String id);
}