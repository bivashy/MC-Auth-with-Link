package me.mastercapexd.auth.bungee.events;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

public class PlayerPreConnectServerEvent extends Event implements Cancellable {
	private boolean cancelled;
	private final ProxiedPlayer player;
	private final ServerInfo serverInfo;

	public PlayerPreConnectServerEvent(ProxiedPlayer player, ServerInfo serverInfo) {
		this.player = player;
		this.serverInfo = serverInfo;
	}

	public ProxiedPlayer getPlayer() {
		return player;
	}

	public ServerInfo getServerInfo() {
		return serverInfo;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}
}
