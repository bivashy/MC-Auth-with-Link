package me.mastercapexd.auth.task;

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
                                player -> player.disconnect(plugin.getConfig().getServerMessages().getMessage("time-left", new ServerMessageContext(account))));
                plugin.getAuthenticatingAccountBucket().removeAuthorizingAccount(account);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void stop() {
        scheduler.cancel();
    }
}
