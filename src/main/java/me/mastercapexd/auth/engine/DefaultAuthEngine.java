package me.mastercapexd.auth.engine;

import java.util.concurrent.TimeUnit;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.AuthEngine;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.authentication.step.MessageableAuthenticationStep;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.config.message.proxy.ProxyMessageContext;
import me.mastercapexd.auth.link.entryuser.LinkEntryUser;
import me.mastercapexd.auth.proxy.ProxyCore;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.api.bossbar.ProxyBossbar;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;

public class DefaultAuthEngine implements AuthEngine {
    private static final ProxyPlugin PROXY_PLUGIN = ProxyPlugin.instance();
    private static final ProxyCore PROXY_CORE = PROXY_PLUGIN.getCore();
    private static final PluginConfig PLUGIN_CONFIG = PROXY_PLUGIN.getConfig();

    @Override
    public void start() {
        startMessageTask();
        startAuthTask();
    }

    private void startMessageTask() {
        PROXY_CORE.schedule(PROXY_PLUGIN, () -> {
            for (ProxyPlayer player : PROXY_CORE.getPlayers()) {
                String id = PLUGIN_CONFIG.getActiveIdentifierType().getId(player);
                Account account = Auth.getAccount(id);
                if (account == null)
                    continue;
                if (account.getCurrentAuthenticationStep() instanceof MessageableAuthenticationStep)
                    ((MessageableAuthenticationStep) account.getCurrentAuthenticationStep()).process(PROXY_CORE.wrapPlayer(player).get());
            }
        }, 0L, PLUGIN_CONFIG.getMessagesDelay(), TimeUnit.SECONDS);
    }

    private void startAuthTask() {
        PROXY_CORE.schedule(PROXY_PLUGIN, () -> {
            long now = System.currentTimeMillis();
            for (ProxyPlayer player : PROXY_CORE.getPlayers()) {
                String id = PLUGIN_CONFIG.getActiveIdentifierType().getId(player);
                Account account = Auth.getAccount(id);
                if (account == null) {
                    if (Auth.getBar(id) != null) {
                        Auth.getBar(id).removeAll();
                        Auth.removeBar(id);
                    }
                    continue;
                }
                int onlineTime = (int) (now - Auth.getJoinTime(id)) / 1000;

                long authTime = PLUGIN_CONFIG.getAuthTime();
                for (LinkEntryUser entryUser : Auth.getLinkEntryAuth().getLinkUsers(user -> user.getAccount().getId().equals(account.getId())))
                    if (entryUser != null)
                        try {
                            authTime += entryUser.getLinkType().getSettings().getEnterSettings().getEnterDelay();
                        }catch(UnsupportedOperationException ignored){ // If link type has no settings support
                        }

                if (onlineTime >= authTime) {
                    player.disconnect(PLUGIN_CONFIG.getProxyMessages().getMessage("time-left", new ProxyMessageContext(account)));
                    Auth.removeAccount(id);
                    continue;
                }
                if (!PLUGIN_CONFIG.getBossBarSettings().isEnabled())
                    continue;
                if (Auth.getBar(id) == null) {
                    ProxyBossbar bossBar = PLUGIN_CONFIG.getBossBarSettings().createBossBar();
                    bossBar.send(player);
                    Auth.addBar(id, bossBar);
                }
                ProxyBossbar bar = Auth.getBar(id);
                bar.progress(1.0F - onlineTime / (float) authTime);
                bar.update();
            }
        }, 0L, 1000L, TimeUnit.MILLISECONDS);
    }
}
