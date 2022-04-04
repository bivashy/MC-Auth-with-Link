package me.mastercapexd.auth.utils;

import java.util.function.Consumer;
import java.util.logging.Level;

import me.mastercapexd.auth.bungee.events.PlayerPreConnectServerEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Connector {

	public static void connectOrKick(ProxiedPlayer player, ServerInfo serverInfo, BaseComponent[] error) {
		connect(player, serverInfo, p -> p.disconnect(error), null);
	}

	public static void connect(ProxiedPlayer player, ServerInfo serverInfo, Consumer<ProxiedPlayer> onFail,
			Runnable afterConnect) {
		PlayerPreConnectServerEvent preConnectServerEvent = new PlayerPreConnectServerEvent(player, serverInfo);
		ProxyServer.getInstance().getPluginManager().callEvent(preConnectServerEvent);
		if (preConnectServerEvent.isCancelled())
			return;
		if (serverInfo == null) {
			ProxyServer.getInstance().getLogger().log(Level.WARNING,
					"Player: " + player.getName() + " tried to connect in null server");
			onFail.accept(player);
			return;
		}

		if (player.getServer() != null && player.getServer().getInfo().equals(serverInfo))
			return;

		player.connect(serverInfo, (result, exception) -> {
		});
	}
}