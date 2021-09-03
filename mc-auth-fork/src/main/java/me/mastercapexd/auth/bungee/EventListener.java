package me.mastercapexd.auth.bungee;

import me.mastercapexd.auth.Account;
import me.mastercapexd.auth.AccountFactory;
import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.IdentifierType;
import me.mastercapexd.auth.PluginConfig;
import me.mastercapexd.auth.bungee.events.SessionEnterEvent;
import me.mastercapexd.auth.storage.AccountStorage;
import me.mastercapexd.auth.utils.Connector;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class EventListener implements Listener {

	private final PluginConfig config;
	private final AccountFactory accountFactory;
	private final AccountStorage accountStorage;

	public EventListener(PluginConfig config, AccountFactory accountFactory, AccountStorage accountStorage) {
		this.config = config;
		this.accountFactory = accountFactory;
		this.accountStorage = accountStorage;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void on(PreLoginEvent event) {
		String name = event.getConnection().getName();
		if (!config.getNamePattern().matcher(name).matches()) {
			event.setCancelReason(config.getBungeeMessages().getMessage("illegal-name-chars"));
			event.setCancelled(true);
			return;
		}
		if (config.getMaxLoginPerIP() != 0
				&& getOnlineExactIP(event.getConnection().getAddress().getAddress().getHostAddress()) >= config
						.getMaxLoginPerIP()) {
			event.setCancelReason(config.getBungeeMessages().getMessage("limit-ip-reached"));
			event.setCancelled(true);
			return;
		}
		if (!config.isNameCaseCheckEnabled())
			return;

		IdentifierType identifierType = config.getActiveIdentifierType();
		String id = identifierType == IdentifierType.UUID ? event.getConnection().getUniqueId().toString()
				: name.toLowerCase();

		accountStorage.getAccount(id).thenAccept(account -> {
			if (account == null)
				return;
			if (account.getName().equals(name))
				return;
			event.getConnection().disconnect(config.getBungeeMessages().getLegacyMessage("check-name-case-failed")
					.replaceAll("(?i)%correct%", account.getName()).replaceAll("(?i)%failed%", name));
			event.setCancelled(true);
		});
	}

	@EventHandler
	public void on(PostLoginEvent event) {
		ProxiedPlayer player = event.getPlayer();
		String id = config.getActiveIdentifierType().getId(player);
		accountStorage.getAccount(id).thenAccept(account -> {
			if (account == null) {
				@SuppressWarnings("deprecation")
				Account newAccount = accountFactory.createAccount(id, config.getActiveIdentifierType(),
						player.getUniqueId(), player.getName(), config.getActiveHashType(), null, -1, 0,
						player.getAddress().getHostString(), -1, config.getSessionDurability());
				ServerInfo authServer = config.findServerInfo(config.getAuthServers());
				Auth.addAccount(newAccount);
				Connector.connectOrKick(player, authServer,
						config.getBungeeMessages().getMessage("auth-servers-connection-refused"));
			} else {
				if (account.isSessionActive(config.getSessionDurability())) {
					SessionEnterEvent sessionEvent = new SessionEnterEvent(account);
					ProxyServer.getInstance().getPluginManager().callEvent(sessionEvent);
					if (sessionEvent.isCancelled())
						return;
					player.sendMessage(config.getBungeeMessages().getMessage("autoconnect"));
					Auth.removeAccount(id);
				} else {
					ServerInfo authServer = config.findServerInfo(config.getAuthServers());
					Auth.addAccount(account);
					Connector.connectOrKick(player, authServer,
							config.getBungeeMessages().getMessage("auth-servers-connection-refused"));
				}
			}
		});
	}

	@EventHandler
	public void on(ChatEvent event) {
		if (event.isCancelled())
			return;
		ProxiedPlayer player = (ProxiedPlayer) event.getSender();
		if (!Auth.hasAccount(config.getActiveIdentifierType().getId(player)))
			return;
		if (!(event.isCommand() || event.isProxyCommand()))
			if (config.getCaptchaServers().contains(player.getServer().getInfo()))
				return;

		String message = event.getMessage();
		if (!message.toLowerCase().startsWith("/l") && !message.toLowerCase().startsWith("/reg")
				&& !message.toLowerCase().startsWith("/login") && !message.toLowerCase().startsWith("/register")) {
			player.sendMessage(config.getBungeeMessages().getMessage("disabled-command"));
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onConnect(ServerConnectEvent event) {
		ProxiedPlayer player = event.getPlayer();
		String id = config.getActiveIdentifierType().getId(player);
		if (!Auth.hasAccount(id))
			return;
		if (!config.getBlockedServers().contains(event.getTarget()))
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void on(PlayerDisconnectEvent event) {
		String id = config.getActiveIdentifierType().getId(event.getPlayer());
		if (Auth.hasAccount(id)) {
			Auth.removeAccount(id);
			return;
		}

		if (Auth.hasEntryAccount(id))
			Auth.removeEntryAccount(id);

		accountStorage.getAccount(id).thenAccept(account -> {
			account.setLastQuitTime(System.currentTimeMillis());
			accountStorage.saveOrUpdateAccount(account);
		});
	}

	@SuppressWarnings("deprecation")
	private int getOnlineExactIP(String address) {
		int findedExactIPs = 0;
		for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
			if (p.getPendingConnection().getAddress().getAddress().getHostAddress().equals(address)) {
				findedExactIPs++;
			}
		}
		return findedExactIPs;
	}

}