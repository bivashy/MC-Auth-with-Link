package me.mastercapexd.auth.bungee.server;

import java.util.List;
import java.util.stream.Collectors;

import me.mastercapexd.auth.bungee.player.BungeeProxyPlayer;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.proxy.server.Server;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeServer implements Server {
	private final ServerInfo bungeeServerInfo;

	public BungeeServer(ServerInfo bungeeServerInfo) {
		this.bungeeServerInfo = bungeeServerInfo;
	}

	@Override
	public String getServerName() {
		return bungeeServerInfo.getName();
	}

	@Override
	public void sendPlayer(ProxyPlayer... players) {
		for (ProxyPlayer player : players)
			player.<ProxiedPlayer>getRealPlayer().connect(bungeeServerInfo, (result, exception) -> {
			});
	}

	@Override
	public List<ProxyPlayer> getPlayers() {
		return bungeeServerInfo.getPlayers().stream().map(bungeePlayer -> new BungeeProxyPlayer(bungeePlayer))
				.collect(Collectors.toList());
	}

	@Override
	public int getPlayersCount() {
		return bungeeServerInfo.getPlayers().size();
	}

	@Override
	public boolean isExists() {
		return bungeeServerInfo != null;
	}
}
