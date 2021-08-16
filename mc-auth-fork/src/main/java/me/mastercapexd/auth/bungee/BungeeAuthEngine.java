package me.mastercapexd.auth.bungee;

import java.util.concurrent.TimeUnit;

import me.mastercapexd.auth.Account;
import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.AuthEngine;
import me.mastercapexd.auth.PluginConfig;
import me.mastercapexd.auth.objects.Server;
import me.mastercapexd.auth.utils.Connector;
import me.mastercapexd.auth.utils.TitleBar;
import me.mastercapexd.auth.utils.bossbar.BossBar;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class BungeeAuthEngine implements AuthEngine {

	private final Plugin plugin;

	private final PluginConfig config;

	private ScheduledTask authTask;

	private ScheduledTask messageTask;

	public BungeeAuthEngine(Plugin plugin, PluginConfig config, ScheduledTask authTask, ScheduledTask messageTask) {
		this.plugin = plugin;
		this.config = config;
		this.authTask = authTask;
		this.messageTask = messageTask;
	}

	public BungeeAuthEngine(Plugin plugin, PluginConfig config) {
		this.plugin = plugin;
		this.config = config;
	}

	@Override
	public void start() {
		startMessageTask();
		startAuthTask();
	}

	private void startMessageTask() {
		this.messageTask = ProxyServer.getInstance().getScheduler().schedule(this.plugin, () -> {
			for (Server server : this.config.getAuthServers()) {
				ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(server.getId());
				if (serverInfo == null)
					continue;
				for (ProxiedPlayer player : serverInfo.getPlayers()) {
					String id = this.config.getActiveIdentifierType().getId(player);
					Account account = Auth.getAccount(id);
					if (account != null) {
						if (Auth.hasEntryAccount(account.getId())) {
							player.sendMessage(
									this.config.getBungeeMessages().getMessage("vk-enter-confirm-need-chat"));
							TitleBar.send(player,
									this.config.getBungeeMessages().getLegacyMessage("vk-enter-confirm-need-title"),
									this.config.getBungeeMessages().getLegacyMessage("vk-enter-confirm-need-subtitle"),
									0, 120, 0);
							continue;
						}
						if (account.isRegistered()) {
							player.sendMessage(this.config.getBungeeMessages().getMessage("login-chat"));
							TitleBar.send(player, this.config.getBungeeMessages().getLegacyMessage("login-title"),
									this.config.getBungeeMessages().getLegacyMessage("login-subtitle"), 0, 120, 0);
							continue;
						}
						player.sendMessage(this.config.getBungeeMessages().getMessage("register-chat"));
						TitleBar.send(player, this.config.getBungeeMessages().getLegacyMessage("register-title"),
								this.config.getBungeeMessages().getLegacyMessage("register-subtitle"), 0, 120, 0);
					}
				}
			}
		}, 0L, this.config.getMessagesDelay(), TimeUnit.SECONDS);
	}

	private void startAuthTask() {
		this.authTask = ProxyServer.getInstance().getScheduler().schedule(this.plugin, () -> {
			long now = System.currentTimeMillis();
			for (Server server : this.config.getAuthServers()) {
				ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(server.getId());
				if (serverInfo == null)
					continue;
				for (ProxiedPlayer player : serverInfo.getPlayers()) {
					String id = this.config.getActiveIdentifierType().getId(player);
					Account account = Auth.getAccount(id);
					if (account == null) {
						if (Auth.getBar(id) != null) {
							Auth.getBar(id).removeAll();
							Auth.removeBar(id);
						}
						ServerInfo connectServer = this.config.findServerInfo(this.config.getGameServers());
						Connector.connectOrKick(player, connectServer,
								this.config.getBungeeMessages().getMessage("game-servers-connection-refused"));
						continue;
					}
					int onlineTime = (int) (now - Auth.getJoinTime(id)) / 1000;
					long authTime = Auth.hasEntryAccount(account.getId()) ? (this.config.getAuthTime() * 2L)
							: this.config.getAuthTime();
					if (onlineTime >= authTime) {
						player.disconnect(this.config.getBungeeMessages().getMessage("time-left"));
						Auth.removeAccount(id);
						continue;
					}
					if (!this.config.getBossBarSettings().isEnabled())
						continue;
					if (Auth.getBar(id) == null) {
						BossBar bossBar = this.config.getBossBarSettings().createBossBar();
						bossBar.addPlayer(player);
						Auth.addBar(id, bossBar);
					}
					BossBar bar = Auth.getBar(id);
					bar.setProgress(1.0F - onlineTime / (float) authTime);
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
		if (messageTask != null) {
			messageTask.cancel();
			messageTask = null;
		}
	}
}