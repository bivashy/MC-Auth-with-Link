package me.mastercapexd.auth.bungee;

import java.util.concurrent.TimeUnit;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.AuthEngine;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.authentication.step.steps.LoginAuthenticationStep;
import me.mastercapexd.auth.authentication.step.steps.RegisterAuthenticationStep;
import me.mastercapexd.auth.authentication.step.steps.link.GoogleCodeAuthenticationStep;
import me.mastercapexd.auth.authentication.step.steps.link.VKLinkAuthenticationStep;
import me.mastercapexd.auth.bungee.message.BungeeMultiProxyComponent;
import me.mastercapexd.auth.bungee.player.BungeeProxyPlayer;
import me.mastercapexd.auth.bungee.player.BungeeProxyPlayer.BungeeProxyPlayerFactory;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.config.message.Messages;
import me.mastercapexd.auth.config.message.proxy.ProxyMessageContext;
import me.mastercapexd.auth.config.server.ConfigurationServer;
import me.mastercapexd.auth.link.entryuser.LinkEntryUser;
import me.mastercapexd.auth.link.vk.VKLinkType;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.api.bossbar.ProxyBossbar;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import me.mastercapexd.auth.utils.TitleBar;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class BungeeAuthEngine implements AuthEngine {

	private static final Messages<ProxyComponent> GOOGLE_MESSAGES = ProxyPlugin.instance().getConfig()
			.getProxyMessages().getSubMessages("google");
	private static final Messages<ProxyComponent> VK_MESSAGES = ProxyPlugin.instance().getConfig().getProxyMessages()
			.getSubMessages("vk");

	private final Plugin plugin;

	private final PluginConfig config;

	private ScheduledTask authTask;

	private ScheduledTask messageTask;

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
			for (ConfigurationServer server : this.config.getAuthServers()) {
				ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(server.getId());
				if (serverInfo == null)
					continue;
				for (ProxiedPlayer player : serverInfo.getPlayers()) {
					String id = this.config.getActiveIdentifierType()
							.getId(BungeeProxyPlayerFactory.wrapPlayer(player));
					Account account = Auth.getAccount(id);
					if (account == null)
						continue;
					if (VKLinkAuthenticationStep.STEP_NAME
							.equals(account.getCurrentAuthenticationStep().getStepName())) {
						player.sendMessage(
								VK_MESSAGES.getMessage("enter-confirm-need-chat", new ProxyMessageContext(account))
										.as(BungeeMultiProxyComponent.class).components());
						TitleBar.send(player, VK_MESSAGES.getStringMessage("enter-confirm-need-title"),
								VK_MESSAGES.getStringMessage("enter-confirm-need-subtitle"), 0, 120, 0);
						continue;
					}

					if (GoogleCodeAuthenticationStep.STEP_NAME
							.equals(account.getCurrentAuthenticationStep().getStepName())) {
						player.sendMessage(
								GOOGLE_MESSAGES.getMessage("need-code-chat", new ProxyMessageContext(account))
										.as(BungeeMultiProxyComponent.class).components());
						TitleBar.send(player, GOOGLE_MESSAGES.getStringMessage("need-code-title"),
								GOOGLE_MESSAGES.getStringMessage("need-code-subtitle"), 0, 120, 0);
						continue;
					}

					if (LoginAuthenticationStep.STEP_NAME
							.equals(account.getCurrentAuthenticationStep().getStepName())) {
						player.sendMessage(this.config.getProxyMessages()
								.getMessage("login-chat", new ProxyMessageContext(account))
								.as(BungeeMultiProxyComponent.class).components());
						TitleBar.send(player, this.config.getProxyMessages().getStringMessage("login-title"),
								this.config.getProxyMessages().getStringMessage("login-subtitle"), 0, 120, 0);
						continue;
					}

					if (RegisterAuthenticationStep.STEP_NAME
							.equals(account.getCurrentAuthenticationStep().getStepName())) {
						player.sendMessage(this.config.getProxyMessages()
								.getMessage("register-chat", new ProxyMessageContext(account))
								.as(BungeeMultiProxyComponent.class).components());
						TitleBar.send(player, this.config.getProxyMessages().getStringMessage("register-title"),
								this.config.getProxyMessages().getStringMessage("register-subtitle"), 0, 120, 0);
					}
				}
			}
		}, 0L, this.config.getMessagesDelay(), TimeUnit.SECONDS);
	}

	private void startAuthTask() {
		this.authTask = ProxyServer.getInstance().getScheduler().schedule(this.plugin, () -> {
			long now = System.currentTimeMillis();
			for (ConfigurationServer server : this.config.getAuthServers()) {
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

					long authTime = this.config.getAuthTime();
					for (LinkEntryUser entryUser : Auth.getLinkEntryAuth()
							.getLinkUsers(user -> user.getAccount().getId().equals(account.getId())))
						if (entryUser != null)
							authTime += entryUser.getLinkType().getSettings().getEnterSettings().getEnterDelay() * 1000;

					if (onlineTime >= authTime) {
						player.disconnect(
								this.config.getProxyMessages().getMessage("time-left", new ProxyMessageContext(account))
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