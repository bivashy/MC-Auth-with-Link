package me.mastercapexd.auth.task;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.config.bossbar.BossBarSettings;
import com.bivashy.auth.api.event.AccountJoinEvent;
import com.bivashy.auth.api.event.PlayerLogoutEvent;
import com.bivashy.auth.api.model.AuthenticationTask;
import com.bivashy.auth.api.model.PlayerIdSupplier;
import com.bivashy.auth.api.server.bossbar.ServerBossbar;
import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.auth.api.server.player.ServerPlayer;
import com.bivashy.auth.api.server.scheduler.ServerScheduler;

import io.github.revxrsal.eventbus.SubscribeEvent;

public class AuthenticationProgressBarTask implements AuthenticationTask {
    private final Map<String, ServerBossbar> progressBars = new HashMap<>();
    private final BossBarSettings settings;
    private final ServerScheduler proxyScheduler;

    public AuthenticationProgressBarTask(AuthPlugin plugin) {
        this.settings = plugin.getConfig().getBossBarSettings();

        this.proxyScheduler = plugin.getCore().schedule(() -> {
            long now = System.currentTimeMillis();
            long timeoutMillis = plugin.getConfig().getAuthTime();

            Iterator<Entry<String, ServerBossbar>> iterator = progressBars.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry<String, ServerBossbar> entry = iterator.next();
                String accountId = entry.getKey();
                ServerBossbar progressBar = entry.getValue();
                PlayerIdSupplier idSupplier = PlayerIdSupplier.of(accountId);
                Optional<Account> accountOptional = plugin.getAuthenticatingAccountBucket().getAuthenticatingAccount(idSupplier);
                if (!accountOptional.isPresent()) {
                    iterator.remove();
                    progressBar.removeAll();
                    continue;
                }

                Account account = accountOptional.get();
                Optional<ServerPlayer> player = account.getPlayer();
                if (!player.isPresent()) {
                    iterator.remove();
                    progressBar.removeAll();
                    continue;
                }

                long accountTimeElapsedFromEntryMillis = (now -
                        plugin.getAuthenticatingAccountBucket().getEnterTimestampOrZero(idSupplier));

                if (progressBar.players().isEmpty())
                    progressBar.send(player.get());

                float progress = 1.0F - (accountTimeElapsedFromEntryMillis / (float) timeoutMillis);
                if (progress > 1 || progress < 0) {
                    iterator.remove();
                    progressBar.removeAll();
                    continue;
                }

                String formattedDuration = settings.getDurationPlaceholderFormat().format(new Date(timeoutMillis - accountTimeElapsedFromEntryMillis));
                progressBar.title(ServerComponent.fromJson(settings.getTitle().jsonText().replace("%duration%", formattedDuration)));
                progressBar.progress(progress);
                progressBar.update();
            }
        }, 0, 1, TimeUnit.SECONDS);
        plugin.getEventBus().register(this);
    }

    @Override
    public void stop() {
        proxyScheduler.cancel();
    }

    @SubscribeEvent
    public void onJoin(AccountJoinEvent e) {
        registerBossbar(e.getAccount());
    }

    @SubscribeEvent
    public void onLogout(PlayerLogoutEvent e) {
        registerBossbar(e.getPlayer());
    }

    private void registerBossbar(PlayerIdSupplier playerIdSupplier) {
        ServerBossbar bossbar = settings.createBossbar();
        if (bossbar == null)
            return;
        progressBars.put(playerIdSupplier.getPlayerId(), bossbar);
    }
}
