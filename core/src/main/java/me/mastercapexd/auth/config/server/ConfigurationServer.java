package me.mastercapexd.auth.config.server;

import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.server.Server;

public class ConfigurationServer {
    private final String id;
    private final int maxPlayers;

    public ConfigurationServer(String stringFormat) {
        String[] args = stringFormat.split(":");
        this.id = args[0];
        this.maxPlayers = args.length >= 2 ? Integer.parseInt(args[1]) : Integer.MAX_VALUE;
    }

    public ConfigurationServer(String id, int maxPlayers) {
        this.id = id;
        this.maxPlayers = maxPlayers;
    }

    public Server asProxyServer() {
        return ProxyPlugin.instance().getCore().serverFromName(id).orElseThrow(() -> new NullPointerException("Server with name " + id + " not exists!"));
    }

    public String getId() {
        return id;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }
}