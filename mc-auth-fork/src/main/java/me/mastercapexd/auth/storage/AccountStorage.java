package me.mastercapexd.auth.storage;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import me.mastercapexd.auth.account.Account;

public interface AccountStorage {

	CompletableFuture<Account> getAccount(String id);

	CompletableFuture<Collection<Account>> getAccountsByVKID(Integer id);

	CompletableFuture<Collection<Account>> getAccounts(int limit);

	CompletableFuture<Collection<Account>> getAllAccounts();
	
	CompletableFuture<Collection<Account>> getAllLinkedAccounts();

	CompletableFuture<Collection<Integer>> getVKIDs();

	void saveOrUpdateAccount(Account account);

	void deleteAccount(String id);

	default void deleteAccount(Account account) {
		deleteAccount(account.getId());
	}
}