package com.bivashy.auth.api.config.server;

import com.bivashy.auth.api.server.proxy.ProxyServer;

public interface ConfigurationServer {
    ProxyServer asProxyServer();

    String getId();

    int getMaxPlayers();
}