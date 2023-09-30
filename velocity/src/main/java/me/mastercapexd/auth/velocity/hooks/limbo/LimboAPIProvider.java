package me.mastercapexd.auth.velocity.hooks.limbo;

import com.bivashy.auth.api.server.proxy.ProxyServer;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.PluginContainer;

import me.mastercapexd.auth.velocity.VelocityAuthPluginBootstrap;
import net.elytrium.limboapi.api.Limbo;
import net.elytrium.limboapi.api.LimboFactory;
import net.elytrium.limboapi.api.chunk.Dimension;
import net.elytrium.limboapi.api.event.LoginLimboRegisterEvent;

public class LimboAPIProvider {

    public static final String LIMBO_API_NAME = "limboapi";
    private static final VelocityAuthPluginBootstrap PLUGIN = VelocityAuthPluginBootstrap.getInstance();
    private final ProxyServer proxyServerWrapper;

    public LimboAPIProvider() {
        LimboFactory limboFactory = (LimboFactory) PLUGIN
                .getProxyServer()
                .getPluginManager()
                .getPlugin(LIMBO_API_NAME)
                .flatMap(PluginContainer::getInstance)
                .orElseThrow(NullPointerException::new);
        Limbo limbo = limboFactory.createLimbo(limboFactory.createVirtualWorld(Dimension.THE_END, 0, 0, 0, 0f, 0f));
        this.proxyServerWrapper = new LimboAPIServer("limbo", limbo);
    }

    public ProxyServer getProxyServerWrapper() {
        return proxyServerWrapper;
    }

    @Subscribe
    public void onLimboLogin(LoginLimboRegisterEvent e) {
        PLUGIN.getAuthPlugin().getCore().wrapPlayer(e.getPlayer()).ifPresent(PLUGIN.getAuthPlugin().getLoginManagement()::onLogin);
    }

}
