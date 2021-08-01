package me.mastercapexd.auth.bungee;

import java.util.concurrent.TimeUnit;

import me.mastercapexd.auth.Account;
import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.AuthEngine;
import me.mastercapexd.auth.PluginConfig;
import me.mastercapexd.auth.objects.Server;
import me.mastercapexd.auth.utils.Connector;
import me.mastercapexd.auth.utils.TitleBar;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class BungeeAuthEngine implements AuthEngine {

	private final Plugin plugin;

	private final PluginConfig config;
	private ScheduledTask authTask;

	public BungeeAuthEngine(Plugin plugin, PluginConfig config, ScheduledTask authTask) {
		this.plugin = plugin;
		this.config = config;
		this.authTask = authTask;
	}

	public BungeeAuthEngine(Plugin plugin, PluginConfig config) {
		this.plugin = plugin;
		this.config = config;
	}

	@Override
	public void start() {
		this.authTask = ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {

			long now = System.currentTimeMillis();

			for (Server server : config.getAuthServers()) {

				ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(server.getId());
				if (serverInfo == null)
					continue;

				for (ProxiedPlayer player : serverInfo.getPlayers()) {
					String id = config.getActiveIdentifierType().getId(player);
					Account account = Auth.getAccount(id);
					if (account != null) {
						
						if ((now - Auth.getJoinTime(id))
								/ 1000 >= (Auth.hasEntryAccount(account.getId()) ? config.getAuthTime() * 2
										: config.getAuthTime())) {
							player.disconnect(config.getBungeeMessages().getMessage("time-left"));
							Auth.removeAccount(id);
							continue;
						}
						if (Auth.hasEntryAccount(account.getId())) {
							player.sendMessage(config.getBungeeMessages().getMessage("vk-enter-confirm-need-chat"));
							TitleBar.send(player,
									config.getBungeeMessages().getLegacyMessage("vk-enter-confirm-need-title"),
									config.getBungeeMessages().getLegacyMessage("vk-enter-confirm-need-subtitle"), 0,
									120, 0);
							continue;
						}
						if (account.isRegistered()) {
							player.sendMessage(config.getBungeeMessages().getMessage("login-chat"));
							TitleBar.send(player, config.getBungeeMessages().getLegacyMessage("login-title"),
									config.getBungeeMessages().getLegacyMessage("login-subtitle"), 0, 120, 0);
						} else {
							player.sendMessage(config.getBungeeMessages().getMessage("register-chat"));
							TitleBar.send(player, config.getBungeeMessages().getLegacyMessage("register-title"),
									config.getBungeeMessages().getLegacyMessage("register-subtitle"), 0, 120, 0);
						}
					} else {
						ServerInfo connectServer = config.findServerInfo(config.getGameServers());
						Connector.connectOrKick(player,connectServer,config.getBungeeMessages().getMessage("game-servers-connection-refused"));
					}
				}

			}
		}, 0L, 1000L, TimeUnit.MILLISECONDS);
	}

	@Override
	public void stop() {
		if (authTask != null) {
			authTask.cancel();
			authTask = null;
		}
	}
}