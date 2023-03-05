package me.mastercapexd.auth.config.server;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.server.ConfigurationServer;
import com.bivashy.auth.api.server.proxy.ProxyServer;

public class BaseConfigurationServer implements ConfigurationServer {
    private final String id;
    private final int maxPlayers;

    public BaseConfigurationServer(String stringFormat) {
        String[] args = stringFormat.split(":");
        this.id = args[0];
        this.maxPlayers = args.length >= 2 ? Integer.parseInt(args[1]) : Integer.MAX_VALUE;
    }

    public BaseConfigurationServer(String id, int maxPlayers) {
        this.id = id;
        this.maxPlayers = maxPlayers;
    }

    @Override
    public ProxyServer asProxyServer() {
        return AuthPlugin.instance().getCore().serverFromName(id).orElseThrow(() -> new NullPointerException("Server with name " + id + " not exists!"));
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int getMaxPlayers() {
        return maxPlayers;
    }
}