package me.mastercapexd.auth.bungee;

import java.util.concurrent.TimeUnit;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.AuthEngine;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.bungee.message.BungeeMultiProxyComponent;
import me.mastercapexd.auth.bungee.player.BungeeProxyPlayer;
import me.mastercapexd.auth.bungee.player.BungeeProxyPlayer.BungeeProxyPlayerFactory;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.config.server.Server;
import me.mastercapexd.auth.link.vk.VKLinkType;
import me.mastercapexd.auth.proxy.api.bossbar.ProxyBossbar;
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
					String id = this.config.getActiveIdentifierType()
							.getId(BungeeProxyPlayerFactory.wrapPlayer(player));
					Account account = Auth.getAccount(id);
					if (account != null) {
						if (Auth.getLinkEntryAuth().hasLinkUser(account.getId(), VKLinkType.getInstance())) {
							player.sendMessage(this.config.getBungeeMessages().getMessage("vk-enter-confirm-need-chat")
									.as(BungeeMultiProxyComponent.class).components());
							TitleBar.send(player,
									this.config.getBungeeMessages().getStringMessage("vk-enter-confirm-need-title"),
									this.config.getBungeeMessages().getStringMessage("vk-enter-confirm-need-subtitle"),
									0, 120, 0);
							continue;
						}
						if (Auth.hasGoogleAuthAccount(account.getId())) {
							player.sendMessage(this.config.getBungeeMessages().getMessage("google-need-code-chat")
									.as(BungeeMultiProxyComponent.class).components());
							TitleBar.send(player,
									this.config.getBungeeMessages().getStringMessage("google-need-code-title"),
									this.config.getBungeeMessages().getStringMessage("google-need-code-subtitle"), 0,
									120, 0);
							continue;
						}
						if (account.isRegistered()) {
							player.sendMessage(this.config.getBungeeMessages().getMessage("login-chat")
									.as(BungeeMultiProxyComponent.class).components());
							TitleBar.send(player, this.config.getBungeeMessages().getStringMessage("login-title"),
									this.config.getBungeeMessages().getStringMessage("login-subtitle"), 0, 120, 0);
							continue;
						}
						player.sendMessage(this.config.getBungeeMessages().getMessage("register-chat")
								.as(BungeeMultiProxyComponent.class).components());
						TitleBar.send(player, this.config.getBungeeMessages().getStringMessage("register-title"),
								this.config.getBungeeMessages().getStringMessage("register-subtitle"), 0, 120, 0);
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
					String id = this.config.getActiveIdentifierType()
							.getId(BungeeProxyPlayerFactory.wrapPlayer(player));
					Account account = Auth.getAccount(id);
					if (account == null) {
						if (Auth.getBar(id) != null) {
							Auth.getBar(id).removeAll();
							Auth.removeBar(id);
						}
						continue;
					}
					int onlineTime = (int) (now - Auth.getJoinTime(id)) / 1000;

					long authTime = Auth.getLinkEntryAuth().hasLinkUser(account.getId(), VKLinkType.getInstance())
							? (this.config.getAuthTime() * 2L)
							: this.config.getAuthTime();
					if (onlineTime >= authTime) {
						player.disconnect(this.config.getBungeeMessages().getMessage("time-left")
								.as(BungeeMultiProxyComponent.class).components());
						Auth.removeAccount(id);
						continue;
					}
					if (!this.config.getBossBarSettings().isEnabled())
						continue;
					if (Auth.getBar(id) == null) {
						ProxyBossbar bossBar = this.config.getBossBarSettings().createBossBar();
						bossBar.send(new BungeeProxyPlayer(player));
						Auth.addBar(id, bossBar);
					}
					ProxyBossbar bar = Auth.getBar(id);
					bar.progress(1.0F - onlineTime / (float) authTime);
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