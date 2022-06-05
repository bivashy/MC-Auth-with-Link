package me.mastercapexd.auth.messenger.commands;

import java.util.Optional;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.link.confirmation.LinkConfirmationUser;
import me.mastercapexd.auth.link.user.LinkUser;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;
import me.mastercapexd.auth.link.user.info.identificator.LinkUserIdentificator;
import me.mastercapexd.auth.messenger.commands.exception.MessengerExceptionHandler;
import me.mastercapexd.auth.messenger.commands.parameters.MessengerLinkContext;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.commands.annotations.GoogleUse;
import me.mastercapexd.auth.proxy.commands.parameters.NewPassword;
import me.mastercapexd.auth.storage.AccountStorage;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.exception.SendMessageException;
import revxrsal.commands.orphan.Orphans;

public abstract class MessengerCommandRegistry {
	private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();
	private final CommandHandler commandHandler;
	private final LinkType linkType;

	public MessengerCommandRegistry(CommandHandler commandHandler, LinkType linkType) {
		this.commandHandler = commandHandler;
		this.linkType = linkType;
		register();
	}

	private void register() {
		registerContexts();
		registerDependencies();
	}

	private void registerContexts() {
		commandHandler.setExceptionHandler(new MessengerExceptionHandler(linkType));

		commandHandler.registerContextValue(LinkType.class, linkType);

		commandHandler.registerCondition((actor, command, arguments) -> {
			if (!command.hasAnnotation(GoogleUse.class))
				return;
			if (!PLUGIN.getConfig().getGoogleAuthenticatorSettings().isEnabled())
				throw new SendMessageException(
						PLUGIN.getConfig().getProxyMessages().getSubMessages("google").getStringMessage("disabled"));
		});

		commandHandler.registerValueResolver(NewPassword.class, context -> {
			String newRawPassword = context.pop();
			if (newRawPassword.length() < PLUGIN.getConfig().getPasswordMinLength())
				throw new SendMessageException(
						PLUGIN.getConfig().getProxyMessages().getStringMessage("password-too-short"));

			if (newRawPassword.length() > PLUGIN.getConfig().getPasswordMaxLength())
				throw new SendMessageException(
						PLUGIN.getConfig().getProxyMessages().getStringMessage("password-too-long"));
			return new NewPassword(newRawPassword);
		});

		commandHandler.registerValueResolver(MessengerLinkContext.class, (context) -> {
			String code = context.popForParameter();
			LinkCommandActorWrapper commandActor = context.actor().as(LinkCommandActorWrapper.class);

			LinkConfirmationUser confirmationUser = Auth.getLinkConfirmationAuth()
					.getLinkUsers(linkUser -> linkUser.getLinkType().equals(linkType)
							&& linkUser.getLinkUserInfo().getIdentificator().equals(commandActor.userId()))
					.stream().findFirst().orElse(null);

			if (confirmationUser == null)
				throw new SendMessageException(linkType.getSettings().getMessages().getMessage("confirmation-no-code"));

			if (System.currentTimeMillis() > confirmationUser.getLinkTimeoutMillis())
				throw new SendMessageException(
						linkType.getSettings().getMessages().getMessage("confirmation-timed-out"));

			if (!confirmationUser.getConfirmationInfo().getConfirmationCode().equals(code))
				throw new SendMessageException(linkType.getSettings().getMessages().getMessage("confirmation-error"));

			LinkUserInfo linkUserInfo = confirmationUser.getAccount()
					.findFirstLinkUser(user -> user.getLinkType().equals(linkType)).get().getLinkUserInfo();

			if (!linkUserInfo.getIdentificator().equals(linkType.getDefaultIdentificator()))
				throw new SendMessageException(linkType.getSettings().getMessages()
						.getMessage("confirmation-already-linked"));

			return new MessengerLinkContext(code, confirmationUser);
		});

		commandHandler.registerValueResolver(Account.class, (context) -> {
			String playerName = context.popForParameter();
			LinkUserIdentificator userId = context.actor().as(LinkCommandActorWrapper.class).userId();
			Account account = PLUGIN.getAccountStorage().getAccountFromName(playerName).get();
			if (account == null || !account.isRegistered())
				throw new SendMessageException(linkType.getSettings().getMessages().getMessage("account-not-found"));

			Optional<LinkUser> linkUser = account.findFirstLinkUser(user -> user.getLinkType().equals(linkType));
			if (!linkUser.isPresent())
				throw new SendMessageException(linkType.getSettings().getMessages().getMessage("not-your-account"));

			if (!linkUser.get().getLinkUserInfo().getIdentificator().equals(userId)
					&& !linkType.getSettings().isAdministrator(userId))
				throw new SendMessageException(linkType.getSettings().getMessages().getMessage("not-your-account"));
			return account;
		});
	}

	private void registerDependencies() {
		commandHandler.registerDependency(AccountStorage.class, PLUGIN.getAccountStorage());
		commandHandler.registerDependency(PluginConfig.class, PLUGIN.getConfig());
		commandHandler.registerDependency(ProxyPlugin.class, PLUGIN);
		commandHandler.registerDependency(LinkType.class, linkType);
	}

	protected void registerCommands() {
		commandHandler.register(
				Orphans.path(linkType.getSettings().getCommandPaths().getCommandPath("code").getCommandPaths())
						.handler(new LinkCodeCommand()));
		commandHandler.register(Orphans
				.path(linkType.getSettings().getCommandPaths().getCommandPath("confirmation-toggle").getCommandPaths())
				.handler(new ConfirmationToggleCommand()));
		commandHandler.register(
				Orphans.path(linkType.getSettings().getCommandPaths().getCommandPath("accounts").getCommandPaths())
						.handler(new AccountsListCommand()));
		commandHandler.register(Orphans
				.path(linkType.getSettings().getCommandPaths().getCommandPath("account-control").getCommandPaths())
				.handler(new AccountCommand()));
		commandHandler.register(
				Orphans.path(linkType.getSettings().getCommandPaths().getCommandPath("enter-accept").getCommandPaths())
						.handler(new AccountEnterAcceptCommand()));
		commandHandler.register(
				Orphans.path(linkType.getSettings().getCommandPaths().getCommandPath("enter-decline").getCommandPaths())
						.handler(new AccountEnterDeclineCommand()));
		commandHandler.register(
				Orphans.path(linkType.getSettings().getCommandPaths().getCommandPath("kick").getCommandPaths())
						.handler(new KickCommand()));
		commandHandler.register(
				Orphans.path(linkType.getSettings().getCommandPaths().getCommandPath("restore").getCommandPaths())
						.handler(new RestoreCommand()));
		commandHandler.register(
				Orphans.path(linkType.getSettings().getCommandPaths().getCommandPath("unlink").getCommandPaths())
						.handler(new UnlinkCommand()));
		commandHandler.register(
				Orphans.path(linkType.getSettings().getCommandPaths().getCommandPath("change-pass").getCommandPaths())
						.handler(new ChangePasswordCommand()));
		commandHandler.register(
				Orphans.path(linkType.getSettings().getCommandPaths().getCommandPath("google-remove").getCommandPaths())
						.handler(new GoogleUnlinkCommand()));
		commandHandler.register(
				Orphans.path(linkType.getSettings().getCommandPaths().getCommandPath("google").getCommandPaths())
						.handler(new GoogleCommand()));
		commandHandler.register(
				Orphans.path(linkType.getSettings().getCommandPaths().getCommandPath("google-code").getCommandPaths())
						.handler(new GoogleCodeCommand()));
		commandHandler.register(
				Orphans.path(linkType.getSettings().getCommandPaths().getCommandPath("admin-panel").getCommandPaths())
						.handler(new AdminPanelCommand()));
	}
}
