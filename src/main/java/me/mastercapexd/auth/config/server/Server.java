package me.mastercapexd.auth.config.server;

import me.mastercapexd.auth.proxy.ProxyPlugin;

public class Server {
	private final String id;
	private final int maxPlayers;

	public Server(String stringFormat) {
		String[] args = stringFormat.split(":");
		this.id = args[0];
		this.maxPlayers = args.length >= 2 ? Integer.parseInt(args[1]) : 50;
	}

	public Server(String id, int maxPlayers) {
		this.id = id;
		this.maxPlayers = maxPlayers;
	}

	public me.mastercapexd.auth.proxy.server.Server asProxyServer() {
		return ProxyPlugin.instance().getCore().serverFromName(id);
	}

	public String getId() {
		return id;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}
}