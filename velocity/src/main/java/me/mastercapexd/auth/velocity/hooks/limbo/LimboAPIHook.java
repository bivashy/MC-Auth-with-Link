package me.mastercapexd.auth.velocity.hooks.limbo;

import com.bivashy.auth.api.hook.LimboPluginHook;
import com.bivashy.auth.api.server.proxy.ProxyServer;

import me.mastercapexd.auth.velocity.VelocityAuthPluginBootstrap;

public class LimboAPIHook implements LimboPluginHook {

    private static final VelocityAuthPluginBootstrap PLUGIN = VelocityAuthPluginBootstrap.getInstance();
    private LimboAPIProvider provider;

    public LimboAPIHook() {
        if (!canHook())
            return;
        this.provider = new LimboAPIProvider();
        PLUGIN.getProxyServer().getEventManager().register(PLUGIN, provider);
    }

    @Override
    public boolean canHook() {
        try {
            net.elytrium.limboapi.api.LimboFactory.class.getName();
            return true;
        } catch (NoClassDefFoundError e) {
            return false;
        }
    }

    @Override
    public ProxyServer createServer(String serverName) {
        return provider.getProxyServerWrapper();
    }

}
