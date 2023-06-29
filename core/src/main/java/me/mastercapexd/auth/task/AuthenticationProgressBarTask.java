package me.mastercapexd.auth.task;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.event.AccountJoinEvent;
import com.bivashy.auth.api.event.AccountStateClearEvent;
import com.bivashy.auth.api.model.AuthenticationTask;
import com.bivashy.auth.api.model.PlayerIdSupplier;
import com.bivashy.auth.api.server.bossbar.ServerBossbar;
import com.bivashy.auth.api.server.scheduler.ServerScheduler;

import io.github.revxrsal.eventbus.SubscribeEvent;

public class AuthenticationProgressBarTask implements AuthenticationTask {
    private final Map<String, ServerBossbar> progressBars = new HashMap<>();
    private final AuthPlugin plugin;
    private final ServerScheduler proxyScheduler;

    public AuthenticationProgressBarTask(AuthPlugin plugin) {
        this.plugin = plugin;
        this.proxyScheduler = plugin.getCore().schedule(() -> {
            long now = System.currentTimeMillis();
            long authTimeoutMillis = plugin.getConfig().getAuthTime();

            for (String accountPlayerId : plugin.getAuthenticatingAccountBucket().getAccountIdEntries()) {
                Account account = plugin.getAuthenticatingAccountBucket().getAuthenticatingAccountNullable(PlayerIdSupplier.of(accountPlayerId));
                int accountEnterElapsedMillis = (int) (now -
                        plugin.getAuthenticatingAccountBucket().getEnterTimestampOrZero(PlayerIdSupplier.of(accountPlayerId)));
                ServerBossbar progressBar = progressBars.get(account.getPlayerId());
                if (progressBar == null)
                    continue;
                if (progressBar.players().isEmpty())
                    account.getPlayer().ifPresent(progressBar::send);
                progressBar.progress(1.0F - accountEnterElapsedMillis / (float) authTimeoutMillis);
                progressBar.update();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void stop() {
        proxyScheduler.cancel();
    }

    @SubscribeEvent
    public void onJoin(AccountJoinEvent e) {
        progressBars.put(e.getAccount().getPlayerId(), plugin.getConfig().getBossBarSettings().createBossbar());
    }

    @SubscribeEvent
    public void onStateClear(AccountStateClearEvent e) {
        e.getAccount().map(Account::getPlayerId).ifPresent(progressBars::remove);
    }
}
