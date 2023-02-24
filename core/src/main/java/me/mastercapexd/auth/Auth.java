package me.mastercapexd.auth;

import java.util.Collection;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.bucket.LinkAuthenticationBucket;
import me.mastercapexd.auth.link.user.confirmation.LinkConfirmationUser;
import me.mastercapexd.auth.link.user.entry.LinkEntryUser;
import me.mastercapexd.auth.model.PlayerIdSupplier;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.api.bossbar.ProxyBossbar;

@Deprecated
public class Auth {
    private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();

    private Auth() {
    }

    public static synchronized Collection<String> getAccountIds() {
        return PLUGIN.getAuthenticatingAccountBucket().getAccountIdEntries();
    }

    public static synchronized void addAccount(Account account) {
        PLUGIN.getAuthenticatingAccountBucket().addAuthorizingAccount(account);
    }

    public static synchronized void removeAccount(String id) {
        PLUGIN.getAuthenticatingAccountBucket().removeAuthorizingAccount(PlayerIdSupplier.of(id));
    }

    public static synchronized boolean hasAccount(String id) {
        return PLUGIN.getAuthenticatingAccountBucket().isAuthorizing(PlayerIdSupplier.of(id));
    }

    public static synchronized Account getAccount(String id) {
        return PLUGIN.getAuthenticatingAccountBucket().getAuthorizingAccountNullable(PlayerIdSupplier.of(id));
    }

    public static synchronized long getJoinTime(String id) {
        return PLUGIN.getAuthenticatingAccountBucket().getEnterTimestampOrZero(PlayerIdSupplier.of(id));
    }

    public static synchronized void incrementAttempts(String id) {
        throw new UnsupportedOperationException();
    }

    public static synchronized void decrementAttempts(String id) {
        throw new UnsupportedOperationException();
    }

    public static synchronized int getPlayerAttempts(String id) {
        throw new UnsupportedOperationException();
    }

    public static LinkAuthenticationBucket<LinkConfirmationUser> getLinkConfirmationAuth() {
        return PLUGIN.getLinkConfirmationBucket();
    }

    public static LinkAuthenticationBucket<LinkEntryUser> getLinkEntryAuth() {
        return PLUGIN.getLinkEntryBucket();
    }

    public static synchronized void addBar(String user, ProxyBossbar bar) {
        throw new UnsupportedOperationException();
    }

    public static synchronized ProxyBossbar getBar(String user) {
        throw new UnsupportedOperationException();
    }

    public static synchronized void removeBar(String user) {
        throw new UnsupportedOperationException();
    }
}