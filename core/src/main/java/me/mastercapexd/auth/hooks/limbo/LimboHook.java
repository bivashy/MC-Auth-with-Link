package me.mastercapexd.auth.hooks.limbo;

import com.bivashy.auth.api.hook.PluginHook;
import com.bivashy.auth.api.server.proxy.limbo.LimboServerWrapper;

public interface LimboHook extends PluginHook {
    boolean isLimbo(String serverName);

    LimboServerWrapper createLimboWrapper(String serverName);
}
