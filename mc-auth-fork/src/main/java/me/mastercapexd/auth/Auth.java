package me.mastercapexd.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import me.mastercapexd.auth.bungee.events.AccountServerEnterEvent;
import me.mastercapexd.auth.objects.VKConfirmationEntry;
import me.mastercapexd.auth.utils.bossbar.BossBar;
import me.mastercapexd.auth.vk.accounts.VKEntryAccount;
import net.md_5.bungee.api.ProxyServer;

public class Auth {

	private static final Map<String, Account> accounts = Maps.newConcurrentMap();
	private static final Map<String, Long> accountTimes = Maps.newConcurrentMap();
	private static final Map<String, Integer> attempts = Maps.newConcurrentMap();
	private static final Map<String, BossBar> bars = Maps.newConcurrentMap();
	private static final Map<Integer, VKConfirmationEntry> vkConfirmationCodes = Maps.newConcurrentMap();
	private static final Map<String, VKEntryAccount> enterAccounts = Maps.newConcurrentMap();

	public static synchronized Collection<String> getAccountIds() {
		return ImmutableSet.copyOf(accounts.keySet());
	}

	public static synchronized void addAccount(Account account) {
		accounts.put(account.getId(), account);
		accountTimes.put(account.getId(), System.currentTimeMillis());
	}

	public static synchronized void removeAccount(String id) {
		AccountServerEnterEvent accountServerEnterEvent = new AccountServerEnterEvent(accounts.get(id), id);
		ProxyServer.getInstance().getPluginManager().callEvent(accountServerEnterEvent);
		if (accountServerEnterEvent.isCancelled())
			return;
		accounts.remove(id);
		accountTimes.remove(id);
		attempts.remove(id);
		if (Auth.getBar(id) != null) {
			Auth.getBar(id).removeAll();
			Auth.removeBar(id);
		}
	}

	public static synchronized boolean hasAccount(String id) {
		return accounts.containsKey(id);
	}

	public static synchronized Account getAccount(String id) {
		return accounts.getOrDefault(id, null);
	}

	public static synchronized long getJoinTime(String id) {
		return accountTimes.getOrDefault(id, 0L);
	}

	public static synchronized void incrementAttempts(String id) {
		attempts.put(id, getPlayerAttempts(id) + 1);
	}

	public static synchronized void decrementAttempts(String id) {
		attempts.put(id, getPlayerAttempts(id) - 1);
	}

	public static synchronized int getPlayerAttempts(String id) {
		return attempts.getOrDefault(id, 0);
	}

	public static synchronized void addVKConfirmationEntry(String id, Integer vkId, String code) {
		vkConfirmationCodes.put(vkId, new VKConfirmationEntry(id, code));
	}

	public static synchronized void removeVKConfirmationEntry(Integer vkId) {
		vkConfirmationCodes.remove(vkId);
	}

	public static synchronized VKConfirmationEntry getVKConfirmationEntry(Integer vkId) {
		return vkConfirmationCodes.get(vkId);
	}

	public static synchronized boolean hasEntryAccount(String id) {
		return enterAccounts.containsKey(id);
	}

	public static synchronized boolean hasEntryAccount(Integer vkId, Integer delay) {
		return getEntryAccount(vkId, delay).size() > 0;
	}

	public static synchronized VKEntryAccount getEntryAccount(String id) {
		return enterAccounts.get(id);
	}

	public static synchronized List<VKEntryAccount> getEntryAccount(Integer vkId, Integer delay) {
		List<VKEntryAccount> accounts = new ArrayList<>();
		for (VKEntryAccount account : enterAccounts.values()) {
			if (account.hasCooldownPassed(delay))
				continue;
			if (account.getVkId().intValue() == vkId.intValue())
				accounts.add(account);
		}
		return accounts;
	}

	public static synchronized VKEntryAccount getEntryAccountByButtonUUID(String buttonUUID) {
		for (VKEntryAccount account : enterAccounts.values()) {
			if (account.getButtonUuid().equals(buttonUUID))
				return account;
		}
		return null;
	}

	public static synchronized void addEntryAccount(Account account, Integer vkId) {
		enterAccounts.put(account.getId(), new VKEntryAccount(account, vkId));
	}

	public static synchronized void removeEntryAccount(String id) {
		enterAccounts.remove(id);
	}

	public static synchronized void addBar(String user, BossBar bar) {
		if (bars.containsKey(user))
			bars.get(user).removeAll();
		bars.put(user, bar);
	}

	public static synchronized BossBar getBar(String user) {
		return bars.get(user);
	}

	public static synchronized void removeBar(String user) {
		bars.remove(user);
	}
}