package me.mastercapexd.auth.storage;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.user.info.identificator.LinkUserIdentificator;

public interface AccountStorage {

	/**
	 * Returns configuration name of storage. For example: MYSQL, SQLITE
	 * 
	 * @return storage name in configuration.
	 */
	String getConfigurationName();

	CompletableFuture<Account> getAccount(String id);

	CompletableFuture<Account> getAccountFromName(String playerName);

	CompletableFuture<Collection<Account>> getAccountsByVKID(Integer id);

	CompletableFuture<Collection<Account>> getAccountsFromLinkIdentificator(LinkUserIdentificator identificator);

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