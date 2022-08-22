package me.mastercapexd.auth.hooks.limbo;

import com.ubivashka.limbo.ProxyLimbo;

public class ProxyLimboHookProvider {
    private final ProxyLimbo instance = ProxyLimbo.instance();

    public boolean isLimbo(String serverName) {
        return instance.getLimboServerContainer().findFirst(limbo -> limbo.getName().equals(serverName)).isPresent();
    }

    public ProxyLimbo get() {
        return instance;
    }
}