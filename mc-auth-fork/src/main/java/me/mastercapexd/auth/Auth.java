package me.mastercapexd.auth;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.bungee.events.AccountServerEnterEvent;
import me.mastercapexd.auth.link.confirmation.LinkConfirmationUser;
import me.mastercapexd.auth.link.entryuser.LinkEntryUser;
import me.mastercapexd.auth.utils.bossbar.BossBar;
import net.md_5.bungee.api.ProxyServer;

public class Auth {

	private Auth() {
	}

	private static final Map<String, Account> ACCOUNTS = Maps.newConcurrentMap();
	private static final Map<String, Long> ACCOUNT_JOIN_TIMES = Maps.newConcurrentMap();
	private static final Map<String, Integer> ATTEMPTS = Maps.newConcurrentMap();
	private static final Map<String, BossBar> BARS = Maps.newConcurrentMap();
	private static final Map<String, Account> googleAuthAccounts = Maps.newConcurrentMap();

	private static final LinkAuth<LinkConfirmationUser> LINK_CONFIRMATION_AUTH = new LinkAuth<>();
	private static final LinkAuth<LinkEntryUser> LINK_ENTRY_AUTH = new LinkAuth<>();

	public static synchronized Collection<String> getAccountIds() {
		return ImmutableSet.copyOf(ACCOUNTS.keySet());
	}

	public static synchronized void addAccount(Account account) {
		ACCOUNTS.put(account.getId(), account);
		ACCOUNT_JOIN_TIMES.put(account.getId(), System.currentTimeMillis());
	}

	public static synchronized void removeAccount(String id) {
		AccountServerEnterEvent accountServerEnterEvent = new AccountServerEnterEvent(ACCOUNTS.get(id), id);
		ProxyServer.getInstance().getPluginManager().callEvent(accountServerEnterEvent);
		if (accountServerEnterEvent.isCancelled())
			return;
		ACCOUNTS.remove(id);
		ATTEMPTS.remove(id);
		ACCOUNT_JOIN_TIMES.remove(id);

		if (Auth.getBar(id) != null) {
			Auth.getBar(id).removeAll();
			Auth.removeBar(id);
		}
	}

	public static synchronized boolean hasAccount(String id) {
		return ACCOUNTS.containsKey(id);
	}

	public static synchronized Account getAccount(String id) {
		return ACCOUNTS.getOrDefault(id, null);
	}

	public static synchronized long getJoinTime(String id) {
		return ACCOUNT_JOIN_TIMES.getOrDefault(id, 0L);
	}

	public static synchronized void incrementAttempts(String id) {
		ATTEMPTS.put(id, getPlayerAttempts(id) + 1);
	}

	public static synchronized void decrementAttempts(String id) {
		ATTEMPTS.put(id, getPlayerAttempts(id) - 1);
	}

	public static synchronized int getPlayerAttempts(String id) {
		return ATTEMPTS.getOrDefault(id, 0);
	}

	public static LinkAuth<LinkConfirmationUser> getLinkConfirmationAuth() {
		return LINK_CONFIRMATION_AUTH;
	}

	public static LinkAuth<LinkEntryUser> getLinkEntryAuth() {
		return LINK_ENTRY_AUTH;
	}

	public static synchronized void addBar(String user, BossBar bar) {
		if (BARS.containsKey(user))
			BARS.get(user).removeAll();
		BARS.put(user, bar);
	}

	public static synchronized BossBar getBar(String user) {
		return BARS.get(user);
	}

	public static synchronized void removeBar(String user) {
		BARS.remove(user);
	}

	public static synchronized void addGoogleAuthAccount(Account account) {
		googleAuthAccounts.put(account.getId(), account);
	}

	public static synchronized void removeGoogleAuthAccount(String id) {
		googleAuthAccounts.remove(id);
	}

	public static synchronized boolean hasGoogleAuthAccount(String id) {
		return googleAuthAccounts.containsKey(id);
	}

	public static synchronized Account getGoogleAuthAccount(String id) {
		return googleAuthAccounts.getOrDefault(id, null);
	}
}