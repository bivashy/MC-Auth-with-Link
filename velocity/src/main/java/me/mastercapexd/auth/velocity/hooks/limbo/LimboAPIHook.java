package me.mastercapexd.auth.velocity.hooks.limbo;

import com.bivashy.auth.api.server.proxy.limbo.LimboServerWrapper;

import me.mastercapexd.auth.hooks.limbo.LimboHook;
import me.mastercapexd.auth.velocity.VelocityAuthPluginBootstrap;

public class LimboAPIHook implements LimboHook {
    private static final VelocityAuthPluginBootstrap PLUGIN = VelocityAuthPluginBootstrap.getInstance();
    private LimboAPIProvider provider;

    public LimboAPIHook() {
        if (!canHook())
            return;
        this.provider = new LimboAPIProvider();
        PLUGIN.getProxyServer().getEventManager().register(PLUGIN, provider);
    }

    @Override
    public boolean isLimbo(String serverName) {
        if (provider == null)
            return false;
        return provider.isLimbo(serverName);
    }

    @Override
    public LimboServerWrapper createLimboWrapper(String serverName) {
        if (provider == null)
            return null;
        return provider.createLimboWrapper(serverName);
    }

    @Override
    public boolean canHook() {
        try {
            net.elytrium.limboapi.api.LimboFactory.class.getName();
            return true;
        } catch(NoClassDefFoundError e) {
            return false;
        }
    }
}
