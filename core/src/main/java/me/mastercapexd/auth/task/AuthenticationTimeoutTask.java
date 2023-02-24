package me.mastercapexd.auth.task;

import java.util.concurrent.TimeUnit;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.config.message.proxy.ProxyMessageContext;
import me.mastercapexd.auth.link.user.entry.LinkEntryUser;
import me.mastercapexd.auth.model.PlayerIdSupplier;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.scheduler.ProxyScheduler;

public class AuthenticationTimeoutTask implements AuthenticationTask {
    private final ProxyScheduler scheduler;

    public AuthenticationTimeoutTask(ProxyPlugin plugin) {
        this.scheduler = plugin.getCore().schedule(plugin, () -> {
            long now = System.currentTimeMillis();
            long authTimeoutMillis = plugin.getConfig().getAuthTime();

            for (String accountPlayerId : plugin.getAuthenticatingAccountBucket().getAccountIdEntries()) {
                Account account = plugin.getAuthenticatingAccountBucket().getAuthorizingAccountNullable(PlayerIdSupplier.of(accountPlayerId));
                int accountEnterElapsedMillis = (int) (now -
                        plugin.getAuthenticatingAccountBucket().getEnterTimestampOrZero(PlayerIdSupplier.of(accountPlayerId)));

                for (LinkEntryUser entryUser : plugin.getLinkEntryBucket().getLinkUsers(user -> user.getAccount().getPlayerId().equals(account.getPlayerId())))
                    if (entryUser != null)
                        try {
                            authTimeoutMillis += entryUser.getLinkType().getSettings().getEnterSettings().getEnterDelay();
                        } catch(UnsupportedOperationException ignored) { // If link type has no settings support
                        }

                if (accountEnterElapsedMillis < authTimeoutMillis)
                    continue;
                account.getPlayer()
                        .ifPresent(
                                player -> player.disconnect(plugin.getConfig().getProxyMessages().getMessage("time-left", new ProxyMessageContext(account))));
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void stop() {
        scheduler.cancel();
    }
}
