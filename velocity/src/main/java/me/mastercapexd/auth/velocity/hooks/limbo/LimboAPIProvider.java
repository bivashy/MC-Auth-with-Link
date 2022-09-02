package me.mastercapexd.auth.velocity.hooks.limbo;

import java.util.List;
import java.util.stream.Collectors;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.PluginContainer;

import me.mastercapexd.auth.proxy.server.limbo.LimboServerWrapper;
import me.mastercapexd.auth.velocity.AuthPlugin;
import me.mastercapexd.auth.velocity.hooks.limbo.config.LimboAPIConfig;
import net.elytrium.limboapi.api.LimboFactory;
import net.elytrium.limboapi.api.event.LoginLimboRegisterEvent;

public class LimboAPIProvider {
    public static final String LIMBO_API_NAME = "limboapi";
    private static final AuthPlugin PLUGIN = AuthPlugin.getInstance();
    private final LimboFactory limboFactory = (LimboFactory) PLUGIN
            .getProxyServer()
            .getPluginManager()
            .getPlugin(LIMBO_API_NAME)
            .flatMap(PluginContainer::getInstance)
            .orElseThrow(NullPointerException::new);
    private final LimboAPIConfig limboAPIConfig = new LimboAPIConfig();
    private final List<LimboServerWrapper> wrappers;

    public LimboAPIProvider() {
        LimboAPIConfig limboConfig = new LimboAPIConfig();
        wrappers = limboConfig.getLimboConfigs()
                .stream()
                .map(config -> new LimboAPIServer(config.getName(), config.createLimbo(limboFactory)))
                .collect(Collectors.toList());
    }

    public LimboServerWrapper createLimboWrapper(String serverName) {
        return wrappers.stream().filter(server -> server.getServerName().equals(serverName)).findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public boolean isLimbo(String serverName) {
        return wrappers.stream().anyMatch(server -> server.getServerName().equals(serverName));
    }

    @Subscribe
    public void onLimboLogin(LoginLimboRegisterEvent e) {
        PLUGIN.getCore().wrapPlayer(e.getPlayer()).ifPresent(PLUGIN.getLoginManagement()::onLogin);
    }
}
