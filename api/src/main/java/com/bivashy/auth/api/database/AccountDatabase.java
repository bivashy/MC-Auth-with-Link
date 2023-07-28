package com.bivashy.auth.api.database;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;

public interface AccountDatabase {
    CompletableFuture<Account> getAccount(String id);

    CompletableFuture<Account> getAccountFromName(String playerName);

    @Deprecated
    CompletableFuture<Collection<Account>> getAccountsByVKID(Integer id);

    CompletableFuture<Collection<Account>> getAccountsFromLinkIdentificator(LinkUserIdentificator identificator);

    CompletableFuture<Collection<Account>> getAllAccounts();

    CompletableFuture<Collection<Account>> getAllLinkedAccounts();

    CompletableFuture<Collection<Account>> getAllLinkedAccounts(LinkType linkType);

    CompletableFuture<Account> saveOrUpdateAccount(Account account);

    CompletableFuture<Void> updateAccountLinks(Account account);

    CompletableFuture<Void> deleteAccount(String id);
}