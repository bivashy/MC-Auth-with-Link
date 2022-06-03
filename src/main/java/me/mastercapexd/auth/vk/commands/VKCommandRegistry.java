package me.mastercapexd.auth.vk.commands;

import java.util.Optional;

import com.ubivashka.lamp.commands.vk.VkActor;
import com.ubivashka.lamp.commands.vk.core.VkHandler;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.account.factories.AccountFactory;
import me.mastercapexd.auth.hooks.VkPluginHook;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.confirmation.LinkConfirmationUser;
import me.mastercapexd.auth.link.user.LinkUser;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;
import me.mastercapexd.auth.link.vk.VKCommandActorWrapper;
import me.mastercapexd.auth.link.vk.VKLinkType;
import me.mastercapexd.auth.messenger.commands.MessengerCommandRegistry;
import me.mastercapexd.auth.messenger.commands.parameters.MessengerLinkContext;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.exception.SendMessageException;

public class VKCommandRegistry extends MessengerCommandRegistry {
	private static final VkPluginHook VK_HOOK = ProxyPlugin.instance().getHook(VkPluginHook.class);
	private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();
	private static final CommandHandler COMMAND_HANDLER = new VkHandler(VK_HOOK.getClient(), VK_HOOK.getActor())
			.disableStackTraceSanitizing();

	public VKCommandRegistry() {
		super(COMMAND_HANDLER, VKLinkType.getInstance());
		COMMAND_HANDLER.registerContextResolver(LinkCommandActorWrapper.class,
				context -> new VKCommandActorWrapper(context.actor()));

		COMMAND_HANDLER.registerValueResolver(Account.class, (context) -> {
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

			if (linkUser.get().getLinkUserInfo().getIdentificator().asNumber() != userId
					|| !PLUGIN.getConfig().getVKSettings().isAdministrator(userId))
				throw new SendMessageException(
						PLUGIN.getConfig().getVKSettings().getMessages().getMessage("not-your-account"));
			return account;
		});

		COMMAND_HANDLER.registerValueResolver(MessengerLinkContext.class, (context) -> {
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
					.get().getLinkUserInfo();

			if (vkLinkUserInfo.getIdentificator().asNumber() != AccountFactory.DEFAULT_VK_ID)
				throw new SendMessageException(
						PLUGIN.getConfig().getVKSettings().getMessages().getMessage("confirmation-already-linked"));

			return new MessengerLinkContext(code, confirmationUser);
		});
		registerCommands();
		
		try {
			VK_HOOK.getClient().groups().setSettings(VK_HOOK.getActor(), VK_HOOK.getActor().getGroupId()).botsCapabilities(true).messages(true).execute();
			VK_HOOK.getClient().groups().setLongPollSettings(VK_HOOK.getActor(), VK_HOOK.getActor().getGroupId()).enabled(true).messageEvent(true).messageNew(true).apiVersion("5.131").execute();
		} catch (ApiException | ClientException e) {
			e.printStackTrace();
			System.err.println("Give all permissions to the vk api token for the automatically settings apply.");
		}
	}
}
