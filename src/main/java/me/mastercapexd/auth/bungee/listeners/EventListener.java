package me.mastercapexd.auth.bungee.listeners;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.account.factories.AccountFactory;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.creators.AuthenticationStepCreator;
import me.mastercapexd.auth.authentication.step.steps.NullAuthenticationStep.NullAuthenticationStepCreator;
import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.bungee.message.BungeeMultiProxyComponent;
import me.mastercapexd.auth.bungee.player.BungeeProxyPlayer.BungeeProxyPlayerFactory;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.storage.AccountStorage;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
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
	public void onPostLogin(PostLoginEvent event) {
		String name = event.getPlayer().getName();
		if (!config.getNamePattern().matcher(name).matches()) {
			event.getPlayer().disconnect(config.getProxyMessages().getMessage("illegal-name-chars")
					.as(BungeeMultiProxyComponent.class).components());
			return;
		}
		if (config.getMaxLoginPerIP() != 0
				&& getOnlineExactIP(event.getPlayer().getAddress().getAddress().getHostAddress()) >= config
						.getMaxLoginPerIP()) {
			event.getPlayer().disconnect(config.getProxyMessages().getMessage("limit-ip-reached")
					.as(BungeeMultiProxyComponent.class).components());
			return;
		}
	}

	@EventHandler
	public void onServerConnected(PostLoginEvent event) {
		ProxiedPlayer player = event.getPlayer();
		startLogin(player);
	}

	@EventHandler
	public void onPlayerChat(ChatEvent event) {
		if (event.isCancelled())
			return;
		if (!(event.getSender() instanceof ProxiedPlayer))
			return;
		ProxyPlayer player = BungeeProxyPlayerFactory.wrapPlayer((ProxiedPlayer) event.getSender());
		if (!Auth.hasAccount(config.getActiveIdentifierType().getId(player)))
			return;

		String message = event.getMessage();
		if (!isAllowedCommand(message)) {
			player.sendMessage(config.getProxyMessages().getStringMessage("disabled-command"));
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockedServerConnect(ServerConnectEvent event) {
		ProxyPlayer player = BungeeProxyPlayerFactory.wrapPlayer(event.getPlayer());
		String id = config.getActiveIdentifierType().getId(player);
		if (!(Auth.hasAccount(id)))
			return;
		if (!config.getBlockedServers().stream().anyMatch(server -> event.getTarget().getName().equals(server.getId())))
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerLeave(PlayerDisconnectEvent event) {
		String id = config.getActiveIdentifierType().getId(BungeeProxyPlayerFactory.wrapPlayer(event.getPlayer()));
		if (Auth.hasAccount(id)) {
			Auth.removeAccount(id);
			return;
		}

		if (Auth.getLinkEntryAuth().hasLinkUser(entryUser -> entryUser.getAccount().getId().equals(id)))
			Auth.getLinkEntryAuth().removeLinkUsers(entryUser -> entryUser.getAccount().getId().equals(id));

		accountStorage.getAccount(id).thenAccept(account -> {
			account.setLastQuitTime(System.currentTimeMillis());
			accountStorage.saveOrUpdateAccount(account);
		});
	}

	@SuppressWarnings("deprecation")
	private long getOnlineExactIP(String address) {
		return ProxyServer.getInstance().getPlayers().stream().filter(proxyPlayer -> proxyPlayer.getPendingConnection()
				.getAddress().getAddress().getHostAddress().equals(address)).count();
	}

	@SuppressWarnings("deprecation")
	private void startLogin(ProxiedPlayer player) {
		String id = config.getActiveIdentifierType().getId(BungeeProxyPlayerFactory.wrapPlayer(player));
		accountStorage.getAccount(id).thenAccept(account -> {
			if (config.isNameCaseCheckEnabled()) {
				if (account != null && !account.getName().equals(player.getName()))
					player.disconnect(config.getProxyMessages().getStringMessage("check-name-case-failed")
							.replaceAll("(?i)%correct%", account.getName())
							.replaceAll("(?i)%failed%", player.getName()));
			}

			AuthenticationStepCreator authenticationStepCreator = AuthPlugin.getInstance()
					.getAuthenticationStepCreatorDealership()
					.findFirstByPredicate(stepCreator -> stepCreator.getAuthenticationStepName().equals(AuthPlugin
							.getInstance().getConfig().getAuthenticationSteps().stream().findFirst().orElse("NULL")))
					.orElse(new NullAuthenticationStepCreator());

			if (account == null) {
				Account newAccount = accountFactory.createAccount(id, config.getActiveIdentifierType(),
						player.getUniqueId(), player.getName(), config.getActiveHashType(),
						AccountFactory.DEFAULT_PASSWORD, AccountFactory.DEFAULT_GOOGLE_KEY,
						AccountFactory.DEFAULT_VK_ID, AccountFactory.DEFAULT_VK_CONFIRMATION_STATE,
						AccountFactory.DEFAULT_LAST_QUIT, player.getAddress().getHostString(),
						AccountFactory.DEFAULT_LAST_SESSION_START, config.getSessionDurability());

				AuthenticationStepContext context = AuthPlugin.getInstance().getAuthenticationContextFactoryDealership()
						.createContext(newAccount);
				newAccount.nextAuthenticationStep(context);

				Auth.addAccount(newAccount);
				return;
			}

			if (!account.getName().equals(player.getName())) {
				player.disconnect(config.getProxyMessages().getStringMessage("check-name-case-failed")
						.replaceAll("(?i)%correct%", account.getName()).replaceAll("(?i)%failed%", player.getName()));
				return;
			}

			AuthenticationStepContext context = AuthPlugin.getInstance().getAuthenticationContextFactoryDealership()
					.createContext(authenticationStepCreator.getAuthenticationStepName(), account);

			if (account.isSessionActive(config.getSessionDurability())) {
				player.sendMessage(config.getProxyMessages().getMessage("autoconnect")
						.as(BungeeMultiProxyComponent.class).components());
				ProxyServer.getInstance().getScheduler().schedule(AuthPlugin.getInstance(),
						() -> account.nextAuthenticationStep(context), config.getJoinDelay(), TimeUnit.MILLISECONDS);
				return;
			}

			Auth.addAccount(account);
			account.nextAuthenticationStep(context);

		});
	}

	private boolean isAllowedCommand(String command) {
		return config.getAllowedCommands().stream().map(s -> Pattern.compile(s))
				.anyMatch(pattern -> pattern.matcher(command).find());
	}

}