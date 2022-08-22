package me.mastercapexd.auth;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.confirmation.LinkConfirmationUser;
import me.mastercapexd.auth.link.entryuser.LinkEntryUser;
import me.mastercapexd.auth.proxy.api.bossbar.ProxyBossbar;

public class Auth {

    private static final Map<String, Account> ACCOUNTS = Collections.synchronizedMap(new HashMap<>());
    private static final Map<String, Long> ACCOUNT_JOIN_TIMES = Collections.synchronizedMap(new HashMap<>());
    private static final Map<String, Integer> ATTEMPTS =  Collections.synchronizedMap(new HashMap<>());
    private static final Map<String, ProxyBossbar> BARS =  Collections.synchronizedMap(new HashMap<>());
    private static final LinkAuth<LinkConfirmationUser> LINK_CONFIRMATION_AUTH = new LinkAuth<>();
    private static final LinkAuth<LinkEntryUser> LINK_ENTRY_AUTH = new LinkAuth<>();

    private Auth() {
    }

    public static synchronized Collection<String> getAccountIds() {
        return Collections.unmodifiableSet(ACCOUNTS.keySet());
    }

    public static synchronized void addAccount(Account account) {
        ACCOUNTS.put(account.getId(), account);
        ACCOUNT_JOIN_TIMES.put(account.getId(), System.currentTimeMillis());
    }

    public static synchronized void removeAccount(String id) {
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

    public static synchronized void addBar(String user, ProxyBossbar bar) {
        if (BARS.containsKey(user))
            BARS.get(user).removeAll();
        BARS.put(user, bar);
    }

    public static synchronized ProxyBossbar getBar(String user) {
        return BARS.get(user);
    }

    public static synchronized void removeBar(String user) {
        BARS.remove(user);
    }
}