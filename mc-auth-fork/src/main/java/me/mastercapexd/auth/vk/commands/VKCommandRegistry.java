package me.mastercapexd.auth.vk.commands;

import java.util.Optional;

import com.ubivashka.lamp.commands.vk.VkActor;
import com.ubivashka.lamp.commands.vk.core.VkHandler;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.account.factories.AccountFactory;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.hooks.VkPluginHook;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.link.confirmation.LinkConfirmationUser;
import me.mastercapexd.auth.link.user.LinkUser;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;
import me.mastercapexd.auth.link.vk.VKCommandActorWrapper;
import me.mastercapexd.auth.link.vk.VKLinkType;
import me.mastercapexd.auth.messenger.commands.AccountCommand;
import me.mastercapexd.auth.messenger.commands.AccountEnterAcceptCommand;
import me.mastercapexd.auth.messenger.commands.AccountEnterDeclineCommand;
import me.mastercapexd.auth.messenger.commands.AccountsListCommand;
import me.mastercapexd.auth.messenger.commands.AdminPanelCommand;
import me.mastercapexd.auth.messenger.commands.ChangePasswordCommand;
import me.mastercapexd.auth.messenger.commands.GoogleCodeCommand;
import me.mastercapexd.auth.messenger.commands.GoogleCommand;
import me.mastercapexd.auth.messenger.commands.GoogleUnlinkCommand;
import me.mastercapexd.auth.messenger.commands.KickCommand;
import me.mastercapexd.auth.messenger.commands.LinkCodeCommand;
import me.mastercapexd.auth.messenger.commands.RestoreCommand;
import me.mastercapexd.auth.messenger.commands.UnlinkCommand;
import me.mastercapexd.auth.messenger.commands.exception.MessengerExceptionHandler;
import me.mastercapexd.auth.messenger.commands.parameters.MessengerLinkContext;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.commands.annotations.GoogleUse;
import me.mastercapexd.auth.proxy.commands.parameters.NewPassword;
import me.mastercapexd.auth.storage.AccountStorage;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.exception.SendMessageException;
import revxrsal.commands.orphan.Orphans;

public class VKCommandRegistry {
	private static final VkPluginHook VK_HOOK = ProxyPlugin.instance().getHook(VkPluginHook.class);
	private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();
	private final CommandHandler commandHandler = new VkHandler(VK_HOOK.getClient(), VK_HOOK.getActor())
			.disableStackTraceSanitizing();

	static {
		ProxyPlugin.instance().getCore().registerListener(PLUGIN, new DispatchCommandListener());
	}

	public VKCommandRegistry() {
		register();
	}

	private void register() {
		registerContexts();
		registerDependencies();
		registerCommands();
	}

	private void registerContexts() {
		commandHandler.setExceptionHandler(new MessengerExceptionHandler(VKLinkType.getInstance()));

		commandHandler.registerContextValue(LinkType.class, VKLinkType.getInstance());

		commandHandler.registerContextResolver(LinkCommandActorWrapper.class,
				context -> new VKCommandActorWrapper(context.actor()));

		commandHandler.registerCondition((actor, command, arguments) -> {
			if (!command.hasAnnotation(GoogleUse.class))
				return;
			if (!PLUGIN.getConfig().getGoogleAuthenticatorSettings().isEnabled())
				throw new SendMessageException(
						PLUGIN.getConfig().getProxyMessages().getSubMessages("google").getStringMessage("disabled"));
		});

		commandHandler.registerValueResolver(Account.class, (context) -> {
			String playerName = context.popForParameter();
			Integer userId = context.actor().as(VkActor.class).getAuthorId();
			Account account = PLUGIN.getAccountStorage().getAccountFromName(playerName).get();
			if (account == null || !account.isRegistered())
				throw new SendMessageException(
						PLUGIN.getConfig().getVKSettings().getMessages().getMessage("account-not-found"));

			Optional<LinkUser> linkUser = account.findFirstLinkUser(VKLinkType.LINK_USER_FILTER);
			if (!linkUser.isPresent())
				throw new SendMessageException(
						PLUGIN.getConfig().getVKSettings().getMessages().getMessage("not-your-account"));

			if (!(linkUser.get().getLinkUserInfo().getIdentificator().asNumber() == userId
					|| PLUGIN.getConfig().getVKSettings().isAdministrator(userId)))
				throw new SendMessageException(
						PLUGIN.getConfig().getVKSettings().getMessages().getMessage("not-your-account"));
			return account;
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
			VkActor commandActor = context.actor().as(VkActor.class);

			LinkConfirmationUser confirmationUser = Auth.getLinkConfirmationAuth()
					.getLinkUsers(linkUser -> linkUser.getLinkType().equals(VKLinkType.getInstance())
							&& linkUser.getLinkUserInfo().getIdentificator().asNumber() == commandActor.getAuthorId())
					.stream().findFirst().orElse(null);

			if (confirmationUser == null)
				throw new SendMessageException(
						PLUGIN.getConfig().getVKSettings().getMessages().getMessage("confirmation-no-code"));

			if (System.currentTimeMillis() > confirmationUser.getLinkTimeoutMillis())
				throw new SendMessageException(
						PLUGIN.getConfig().getVKSettings().getMessages().getMessage("confirmation-timed-out"));

			if (!confirmationUser.getConfirmationInfo().getConfirmationCode().equals(code))
				throw new SendMessageException(
						PLUGIN.getConfig().getVKSettings().getMessages().getMessage("confirmation-error"));

			LinkUserInfo vkLinkUserInfo = confirmationUser.getAccount().findFirstLinkUser(VKLinkType.LINK_USER_FILTER)
					.orElse(null).getLinkUserInfo();

			if (vkLinkUserInfo.getIdentificator().asNumber() != AccountFactory.DEFAULT_VK_ID)
				throw new SendMessageException(
						PLUGIN.getConfig().getVKSettings().getMessages().getMessage("confirmation-already-linked"));

			return new MessengerLinkContext(code, confirmationUser);
		});

	}

	private void registerDependencies() {
		commandHandler.registerDependency(AccountStorage.class, PLUGIN.getAccountStorage());
		commandHandler.registerDependency(PluginConfig.class, PLUGIN.getConfig());
		commandHandler.registerDependency(ProxyPlugin.class, PLUGIN);
		commandHandler.registerDependency(LinkType.class, VKLinkType.getInstance());
	}

	private void registerCommands() {
		commandHandler.register(
				Orphans.path(PLUGIN.getConfig().getVKSettings().getCommandPaths().getPath("code").getCommandPaths())
						.handler(new LinkCodeCommand()));
		commandHandler.register(
				Orphans.path(PLUGIN.getConfig().getVKSettings().getCommandPaths().getPath("accounts").getCommandPaths())
						.handler(new AccountsListCommand()));
		commandHandler.register(
				Orphans.path(PLUGIN.getConfig().getVKSettings().getCommandPaths().getPath("account-control").getCommandPaths())
						.handler(new AccountCommand()));
		commandHandler.register(Orphans
				.path(PLUGIN.getConfig().getVKSettings().getCommandPaths().getPath("enter-accept").getCommandPaths())
				.handler(new AccountEnterAcceptCommand()));
		commandHandler.register(Orphans
				.path(PLUGIN.getConfig().getVKSettings().getCommandPaths().getPath("enter-decline").getCommandPaths())
				.handler(new AccountEnterDeclineCommand()));
		commandHandler.register(
				Orphans.path(PLUGIN.getConfig().getVKSettings().getCommandPaths().getPath("kick").getCommandPaths())
						.handler(new KickCommand()));
		commandHandler.register(
				Orphans.path(PLUGIN.getConfig().getVKSettings().getCommandPaths().getPath("restore").getCommandPaths())
						.handler(new RestoreCommand()));
		commandHandler.register(
				Orphans.path(PLUGIN.getConfig().getVKSettings().getCommandPaths().getPath("unlink").getCommandPaths())
						.handler(new UnlinkCommand()));
		commandHandler.register(Orphans
				.path(PLUGIN.getConfig().getVKSettings().getCommandPaths().getPath("change-pass").getCommandPaths())
				.handler(new ChangePasswordCommand()));
		commandHandler.register(Orphans
				.path(PLUGIN.getConfig().getVKSettings().getCommandPaths().getPath("google-remove").getCommandPaths())
				.handler(new GoogleUnlinkCommand()));
		commandHandler.register(
				Orphans.path(PLUGIN.getConfig().getVKSettings().getCommandPaths().getPath("google").getCommandPaths())
						.handler(new GoogleCommand()));
		commandHandler.register(Orphans
				.path(PLUGIN.getConfig().getVKSettings().getCommandPaths().getPath("google-code").getCommandPaths())
				.handler(new GoogleCodeCommand()));
		commandHandler.register(Orphans
				.path(PLUGIN.getConfig().getVKSettings().getCommandPaths().getPath("admin-panel").getCommandPaths())
				.handler(new AdminPanelCommand()));
	}
}
