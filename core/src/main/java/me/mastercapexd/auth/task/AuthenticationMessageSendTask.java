package me.mastercapexd.auth.task;

import java.util.concurrent.TimeUnit;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.model.AuthenticationTask;
import com.bivashy.auth.api.model.PlayerIdSupplier;
import com.bivashy.auth.api.server.scheduler.ServerScheduler;
import com.bivashy.auth.api.step.MessageableAuthenticationStep;

public class AuthenticationMessageSendTask implements AuthenticationTask {
    private final ServerScheduler proxyScheduler;

    public AuthenticationMessageSendTask(AuthPlugin plugin) {
        this.proxyScheduler = plugin.getCore().schedule(plugin, () -> {
            for (String accountPlayerId : plugin.getAuthenticatingAccountBucket().getAccountIdEntries()) {
                Account account = plugin.getAuthenticatingAccountBucket().getAuthorizingAccountNullable(PlayerIdSupplier.of(accountPlayerId));
                if (!(account.getCurrentAuthenticationStep() instanceof MessageableAuthenticationStep))
                    continue;
                account.getPlayer().ifPresent(player -> ((MessageableAuthenticationStep) account.getCurrentAuthenticationStep()).process(player));
            }
        }, 0, plugin.getConfig().getMessagesDelay(), TimeUnit.SECONDS);
    }

    @Override
    public void stop() {
        proxyScheduler.cancel();
    }
}
