package me.mastercapexd.auth.task;

import java.util.concurrent.TimeUnit;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.authentication.step.MessageableAuthenticationStep;
import me.mastercapexd.auth.model.PlayerIdSupplier;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.scheduler.ProxyScheduler;

public class AuthenticationMessageSendTask implements AuthenticationTask {
    private final ProxyScheduler proxyScheduler;

    public AuthenticationMessageSendTask(ProxyPlugin plugin) {
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
