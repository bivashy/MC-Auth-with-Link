package me.mastercapexd.auth.bungee;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.IdentifierType;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.account.factories.AccountFactory;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.creators.AuthenticationStepCreator;
import me.mastercapexd.auth.authentication.step.steps.NullAuthenticationStep.NullAuthenticationStepCreator;
import me.mastercapexd.auth.bungee.events.SessionEnterEvent;
import me.mastercapexd.auth.config.PluginConfig;
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
			event.getPlayer().disconnect(config.getBungeeMessages().getMessage("illegal-name-chars"));
			return;
		}
		if (config.getMaxLoginPerIP() != 0
				&& getOnlineExactIP(event.getPlayer().getAddress().getAddress().getHostAddress()) >= config
						.getMaxLoginPerIP()) {
			event.getPlayer().disconnect(config.getBungeeMessages().getMessage("limit-ip-reached"));
			return;
		}
		if (!config.isNameCaseCheckEnabled())
			return;

		IdentifierType identifierType = config.getActiveIdentifierType();
		String id = identifierType == IdentifierType.UUID ? String.valueOf(event.getPlayer().getUniqueId())
				: name.toLowerCase();

		accountStorage.getAccount(id).thenAccept(account -> {
			if (account == null)
				return;
			if (account.getName().equals(name))
				return;
			event.getPlayer().disconnect(config.getBungeeMessages().getStringMessage("check-name-case-failed")
					.replaceAll("(?i)%correct%", account.getName()).replaceAll("(?i)%failed%", name));
		});
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
		ProxiedPlayer player = (ProxiedPlayer) event.getSender();
		if (!Auth.hasAccount(config.getActiveIdentifierType().getId(player)))
			return;

		String message = event.getMessage();
		if (!isAllowedCommand(message)) {
			player.sendMessage(config.getBungeeMessages().getMessage("disabled-command"));
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockedServerConnect(ServerConnectEvent event) {
		ProxiedPlayer player = event.getPlayer();
		String id = config.getActiveIdentifierType().getId(player);
		if (!(Auth.hasAccount(id)))
			return;
		if (!config.getBlockedServers().contains(event.getTarget()))
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerLeave(PlayerDisconnectEvent event) {
		String id = config.getActiveIdentifierType().getId(event.getPlayer());
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
		String id = config.getActiveIdentifierType().getId(player);
		accountStorage.getAccount(id).thenAccept(account -> {

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
						.createContext(authenticationStepCreator.getAuthenticationStepName(), newAccount);
				newAccount.nextAuthenticationStep(context);

				Auth.addAccount(newAccount);
				return;
			}

			if (!account.getName().equals(player.getName())) {
				player.disconnect(config.getBungeeMessages().getStringMessage("check-name-case-failed")
						.replaceAll("(?i)%correct%", account.getName()).replaceAll("(?i)%failed%", player.getName()));
				return;
			}

			AuthenticationStepContext context = AuthPlugin.getInstance().getAuthenticationContextFactoryDealership()
					.createContext(authenticationStepCreator.getAuthenticationStepName(), account);

			if (account.isSessionActive(config.getSessionDurability())) {
				SessionEnterEvent sessionEvent = new SessionEnterEvent(account);
				ProxyServer.getInstance().getPluginManager().callEvent(sessionEvent);
				if (sessionEvent.isCancelled())
					return;
				player.sendMessage(config.getBungeeMessages().getMessage("autoconnect"));
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