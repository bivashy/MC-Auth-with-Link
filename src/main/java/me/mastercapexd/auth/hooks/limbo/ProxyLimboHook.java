package me.mastercapexd.auth.hooks.limbo;

import me.mastercapexd.auth.proxy.server.limbo.LimboServerWrapper;
import me.mastercapexd.auth.proxy.server.limbo.ProxyLimboServerWrapper;

public class ProxyLimboHook implements LimboHook {
    private ProxyLimboHookProvider provider;

    public ProxyLimboHook() {
        if (!canHook())
            return;
        provider = new ProxyLimboHookProvider();
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
            return new ProxyLimboServerWrapper(null);
        return new ProxyLimboServerWrapper(provider.get().getLimboServerContainer().findFirst(limbo -> limbo.getName().equals(serverName)).orElse(null));
    }

    @Override
    public boolean canHook() {
        try {
            com.ubivashka.limbo.ProxyLimbo.instance();
            return true;
        } catch(Exception e) {
            return false;
        }
    }
}
