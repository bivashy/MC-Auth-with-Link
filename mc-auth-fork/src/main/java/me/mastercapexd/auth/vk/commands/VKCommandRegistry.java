package me.mastercapexd.auth.vk.commands;

import com.ubivashka.lamp.commands.vk.VkActor;
import com.ubivashka.lamp.commands.vk.core.VkHandler;
import com.ubivashka.vk.api.providers.VkApiProvider;
import com.ubivashka.vk.bungee.BungeeVkApiPlugin;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.account.factories.AccountFactory;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.link.confirmation.LinkConfirmationUser;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;
import me.mastercapexd.auth.link.vk.VKCommandActorWrapper;
import me.mastercapexd.auth.link.vk.VKLinkType;
import me.mastercapexd.auth.messenger.commands.AccountEnterAcceptCommand;
import me.mastercapexd.auth.messenger.commands.AccountsCommand;
import me.mastercapexd.auth.messenger.commands.LinkCodeCommand;
import me.mastercapexd.auth.messenger.commands.parameters.MessengerLinkContext;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.storage.AccountStorage;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.exception.SendMessageException;
import revxrsal.commands.orphan.Orphans;

public class VKCommandRegistry {
	private static final VkApiProvider VK_API_PROVIDER = BungeeVkApiPlugin.getInstance().getVkApiProvider();
	private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();
	private final CommandHandler commandHandler = new VkHandler(VK_API_PROVIDER.getVkApiClient(),
			VK_API_PROVIDER.getActor()).disableStackTraceSanitizing();

	static {
		ProxyPlugin.instance().getCore().registerListener(PLUGIN, new MessageListener());
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
		commandHandler.registerContextValue(LinkType.class, VKLinkType.getInstance());

		commandHandler.registerContextResolver(LinkCommandActorWrapper.class,
				context -> new VKCommandActorWrapper(context.actor()));

		commandHandler.registerContextResolver(VKCommandActorWrapper.class,
				context -> new VKCommandActorWrapper(context.actor()));

		commandHandler.registerValueResolver(MessengerLinkContext.class, (context) -> {
			String code = context.popForParameter();
			VkActor commandActor = context.actor().as(VkActor.class);

			LinkConfirmationUser confirmationUser = Auth.getLinkConfirmationAuth()
					.getLinkUsers(linkUser -> linkUser.getLinkType().equals(VKLinkType.getInstance())
							&& linkUser.getLinkUserInfo().getIdentificator().asNumber()==commandActor.getAuthorId())
					.stream().findFirst().orElse(null);

			if (confirmationUser == null)
				throw new SendMessageException(
						PLUGIN.getConfig().getVKSettings().getVKMessages().getMessage("confirmation-no-code"));

			if (System.currentTimeMillis() > confirmationUser.getLinkTimeoutMillis())
				throw new SendMessageException(
						PLUGIN.getConfig().getVKSettings().getVKMessages().getMessage("confirmation-timed-out"));

			if (!confirmationUser.getConfirmationInfo().getConfirmationCode().equals(code))
				throw new SendMessageException(
						PLUGIN.getConfig().getVKSettings().getVKMessages().getMessage("confirmation-error"));

			LinkUserInfo vkLinkUserInfo = confirmationUser.getAccount()
					.findFirstLinkUser(VKLinkType.getLinkUserPredicate()).orElse(null).getLinkUserInfo();

			if (vkLinkUserInfo.getIdentificator().asNumber() != AccountFactory.DEFAULT_VK_ID)
				throw new SendMessageException(
						PLUGIN.getConfig().getVKSettings().getVKMessages().getMessage("confirmation-already-linked"));

			return new MessengerLinkContext(code, confirmationUser, () -> {
				ProxyPlayer player = PLUGIN.getConfig().getActiveIdentifierType()
						.getPlayer(confirmationUser.getAccount().getId());
				if (player != null)
					player.sendMessage(PLUGIN.getConfig().getProxyMessages().getStringMessage("vk-linked"));

				commandActor
						.reply(PLUGIN.getConfig().getVKSettings().getVKMessages().getMessage("confirmation-success"));
			});
		});

	}

	private void registerDependencies() {
		commandHandler.registerDependency(AccountStorage.class, PLUGIN.getAccountStorage());
		commandHandler.registerDependency(PluginConfig.class, PLUGIN.getConfig());
		commandHandler.registerDependency(ProxyPlugin.class, PLUGIN);
	}

	private void registerCommands() {
		commandHandler.register(
				Orphans.path(PLUGIN.getConfig().getVKSettings().getCommandPaths().getPath("code").getCommandPaths())
						.handler(new LinkCodeCommand()));
		commandHandler.register(
				Orphans.path(PLUGIN.getConfig().getVKSettings().getCommandPaths().getPath("accounts").getCommandPaths())
						.handler(new AccountsCommand()));
		commandHandler.register(Orphans
				.path(PLUGIN.getConfig().getVKSettings().getCommandPaths().getPath("enter-accept").getCommandPaths())
				.handler(new AccountEnterAcceptCommand()));
	}
}
