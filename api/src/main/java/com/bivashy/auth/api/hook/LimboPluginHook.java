package com.bivashy.auth.api.hook;

import com.bivashy.auth.api.server.proxy.ProxyServer;

public interface LimboPluginHook extends PluginHook {

    ProxyServer createServer(String serverName);

}
