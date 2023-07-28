package me.mastercapexd.auth.task;

import java.util.concurrent.TimeUnit;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.model.AuthenticationTask;
import com.bivashy.auth.api.model.PlayerIdSupplier;
import com.bivashy.auth.api.server.scheduler.ServerScheduler;
import com.bivashy.auth.api.step.AuthenticationStep;
import com.bivashy.auth.api.step.MessageableAuthenticationStep;

public class AuthenticationMessageSendTask implements AuthenticationTask {
    private final ServerScheduler proxyScheduler;

    public AuthenticationMessageSendTask(AuthPlugin plugin) {
        this.proxyScheduler = plugin.getCore().schedule(() -> {
            for (String accountPlayerId : plugin.getAuthenticatingAccountBucket().getAccountIdEntries()) {
                Account account = plugin.getAuthenticatingAccountBucket().getAuthenticatingAccountNullable(PlayerIdSupplier.of(accountPlayerId));
                AuthenticationStep authenticationStep = account.getCurrentAuthenticationStep();
                if (!(authenticationStep instanceof MessageableAuthenticationStep))
                    continue;
                account.getPlayer().ifPresent(player -> ((MessageableAuthenticationStep) authenticationStep).process(player));
            }
        }, 0, plugin.getConfig().getMessagesDelay(), TimeUnit.SECONDS);
    }

    @Override
    public void stop() {
        proxyScheduler.cancel();
    }
}
