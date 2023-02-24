package me.mastercapexd.auth.task;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.github.revxrsal.eventbus.SubscribeEvent;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.event.AccountJoinEvent;
import me.mastercapexd.auth.event.AccountStateClearEvent;
import me.mastercapexd.auth.model.PlayerIdSupplier;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.api.bossbar.ProxyBossbar;
import me.mastercapexd.auth.proxy.scheduler.ProxyScheduler;

public class AuthenticationProgressBarTask implements AuthenticationTask {
    private final Map<String, ProxyBossbar> progressBars = new HashMap<>();
    private final ProxyPlugin plugin;
    private final ProxyScheduler proxyScheduler;

    public AuthenticationProgressBarTask(ProxyPlugin plugin) {
        this.plugin = plugin;
        this.proxyScheduler = plugin.getCore().schedule(plugin, () -> {
            long now = System.currentTimeMillis();
            long authTimeoutMillis = plugin.getConfig().getAuthTime();

            for (String accountPlayerId : plugin.getAuthenticatingAccountBucket().getAccountIdEntries()) {
                Account account = plugin.getAuthenticatingAccountBucket().getAuthorizingAccountNullable(PlayerIdSupplier.of(accountPlayerId));
                int accountEnterElapsedMillis = (int) (now -
                        plugin.getAuthenticatingAccountBucket().getEnterTimestampOrZero(PlayerIdSupplier.of(accountPlayerId)));
                ProxyBossbar progressBar = progressBars.get(account.getPlayerId());
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
        progressBars.put(e.getAccount().getPlayerId(), plugin.getConfig().getBossBarSettings().createBossBar());
    }

    @SubscribeEvent
    public void onStateClear(AccountStateClearEvent e) {
        e.getAccount().map(Account::getPlayerId).ifPresent(progressBars::remove);
    }
}
