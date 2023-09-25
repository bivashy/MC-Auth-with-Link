package com.bivashy.auth.api.hook;

import com.bivashy.auth.api.server.proxy.ProxyServer;

public interface LimboPluginHook {

    ProxyServer createServer(String serverName);

}
