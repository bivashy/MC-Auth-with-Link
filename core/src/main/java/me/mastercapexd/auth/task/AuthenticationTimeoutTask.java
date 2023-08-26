package me.mastercapexd.auth.task;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.link.user.entry.LinkEntryUser;
import com.bivashy.auth.api.model.AuthenticationTask;
import com.bivashy.auth.api.model.PlayerIdSupplier;
import com.bivashy.auth.api.server.scheduler.ServerScheduler;

import me.mastercapexd.auth.config.message.server.ServerMessageContext;

public class AuthenticationTimeoutTask implements AuthenticationTask {
    private final ServerScheduler scheduler;

    public AuthenticationTimeoutTask(AuthPlugin plugin) {
        this.scheduler = plugin.getCore().schedule(() -> {
            long now = System.currentTimeMillis();
            long authTimeoutMillis = plugin.getConfig().getAuthTime();

            // Iterator for preventing ConcurrentModificationException, because we are removing element on specific condition
            Iterator<String> accountIdIterator = plugin.getAuthenticatingAccountBucket().getAccountIdEntries().iterator();
            while (accountIdIterator.hasNext()) {
                String accountPlayerId = accountIdIterator.next();
                Account account = plugin.getAuthenticatingAccountBucket().getAuthenticatingAccountNullable(PlayerIdSupplier.of(accountPlayerId));
                int accountEnterElapsedMillis = (int) (now -
                        plugin.getAuthenticatingAccountBucket().getEnterTimestampOrZero(PlayerIdSupplier.of(accountPlayerId)));

                for (LinkEntryUser entryUser : plugin.getLinkEntryBucket().find(user -> user.getAccount().getPlayerId().equals(account.getPlayerId())))
                    if (entryUser != null)
                        try {
                            authTimeoutMillis += entryUser.getLinkType().getSettings().getEnterSettings().getEnterDelay();
                        } catch(UnsupportedOperationException ignored) { // If link type has no settings support
                        }

                if (accountEnterElapsedMillis < authTimeoutMillis)
                    continue;
                account.getPlayer()
                        .ifPresent(
                                player -> player.disconnect(plugin.getConfig().getServerMessages().getMessage("time-left", new ServerMessageContext(account))));
                accountIdIterator.remove();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void stop() {
        scheduler.cancel();
    }
}
