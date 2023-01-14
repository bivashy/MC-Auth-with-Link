package me.mastercapexd.auth.hooks.limbo;

import me.mastercapexd.auth.proxy.hooks.PluginHook;
import me.mastercapexd.auth.proxy.server.limbo.LimboServerWrapper;

public interface LimboHook extends PluginHook {
    boolean isLimbo(String serverName);

    LimboServerWrapper createLimboWrapper(String serverName);
}
