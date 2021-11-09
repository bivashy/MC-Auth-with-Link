package me.mastercapexd.auth.utils;

import java.util.function.Consumer;

import me.mastercapexd.auth.bungee.AuthPlugin;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Connector {

	public static void connectOrKick(ProxiedPlayer player, ServerInfo serverInfo, BaseComponent[] error) {
		connect(player, serverInfo, error, p -> p.disconnect(error), null);
	}

	public static void connect(ProxiedPlayer player, ServerInfo serverInfo, BaseComponent[] error,
			Consumer<ProxiedPlayer> onFail, Runnable afterConnect) {
		if (serverInfo == null) {
			System.out.println(player + " tried to connect in null server");
			player.disconnect(error);
			return;
		}
		if (AuthPlugin.getInstance().getConfig().getConfig().getBoolean("kick-on-server-disabled"))
			serverInfo.ping(new Callback<ServerPing>() {

				@Override
				public void done(ServerPing result, Throwable throwedError) {
					if (throwedError != null) {
						System.out.println(player + " tried to connect in not working server");
						player.disconnect(error);
					}
				}
			});

		if (player.getServer().getInfo().equals(serverInfo))
			return;
		TitleBar.send(player, "", "", 0, 10, 0);
		player.connect(serverInfo, new Callback<Boolean>() {

			@Override
			public void done(Boolean result, Throwable error) {

			}

		});
	}
}