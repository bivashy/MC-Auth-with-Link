package me.mastercapexd.auth.listener;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.config.server.ConfigurationServer;
import com.bivashy.auth.api.event.AccountServerConnectedEvent;
import com.bivashy.auth.api.server.proxy.ProxyServer;
import io.github.revxrsal.eventbus.SubscribeEvent;

import java.util.stream.Collectors;

public class AccountServerChangedListener {
    private final AuthPlugin plugin;
    private final PluginConfig config;

    public AccountServerChangedListener(AuthPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @SubscribeEvent
    public void onAccountServerConnected(AccountServerConnectedEvent e) {
        if (!config.isLicenseSupportEnabled())
            return;

        if (e.getAccount().isPremium() && plugin.getPendingPremiumAccountBucket().isPendingPremium(e.getAccount())) {
            if (config.getGameServers().stream()
                    .map(ConfigurationServer::asProxyServer)
                    .map(ProxyServer::getServerName)
                    .collect(Collectors.toList())
                    .contains(e.getServer().getServerName())) {
                e.getAccount().getPlayer().ifPresent(player -> {
                    player.sendMessage(config.getServerMessages().getMessage("license-verified-successfully-chat"));
                    plugin
                            .getCore()
                            .createTitle(config.getServerMessages().getMessage("license-verified-successfully-title"))
                            .subtitle(config.getServerMessages().getMessage("license-verified-successfully-subtitle"))
                            .stay(80)
                            .fadeOut(10)
                            .send(player);
                });
            }
        }
    }
}
