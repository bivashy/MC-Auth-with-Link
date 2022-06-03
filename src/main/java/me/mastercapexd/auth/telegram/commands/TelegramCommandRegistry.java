package me.mastercapexd.auth.telegram.commands;

import java.util.Optional;

import com.ubivashka.lamp.telegram.TelegramActor;
import com.ubivashka.lamp.telegram.core.TelegramHandler;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.account.factories.AccountFactory;
import me.mastercapexd.auth.hooks.TelegramPluginHook;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.confirmation.LinkConfirmationUser;
import me.mastercapexd.auth.link.telegram.TelegramCommandActorWrapper;
import me.mastercapexd.auth.link.telegram.TelegramLinkType;
import me.mastercapexd.auth.link.user.LinkUser;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;
import me.mastercapexd.auth.messenger.commands.MessengerCommandRegistry;
import me.mastercapexd.auth.messenger.commands.parameters.MessengerLinkContext;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.exception.SendMessageException;

public class TelegramCommandRegistry extends MessengerCommandRegistry {
	private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();
	private static final TelegramPluginHook TELEGRAM_HOOK = PLUGIN.getHook(TelegramPluginHook.class);
	private static final CommandHandler COMMAND_HANDLER = new TelegramHandler(TELEGRAM_HOOK.getTelegramBot());

	public TelegramCommandRegistry() {
		super(COMMAND_HANDLER, TelegramLinkType.getInstance());
		COMMAND_HANDLER.registerContextResolver(LinkCommandActorWrapper.class,
				context -> new TelegramCommandActorWrapper(context.actor()));

		COMMAND_HANDLER.registerValueResolver(Account.class, (context) -> {
			String playerName = context.popForParameter();
			Long userId = context.actor().as(TelegramActor.class).getId();
			Account account = PLUGIN.getAccountStorage().getAccountFromName(playerName).get();
			if (account == null || !account.isRegistered())
				throw new SendMessageException(
						PLUGIN.getConfig().getTelegramSettings().getMessages().getMessage("account-not-found"));

			Optional<LinkUser> linkUser = account.findFirstLinkUser(TelegramLinkType.LINK_USER_FILTER);
			if (!linkUser.isPresent())
				throw new SendMessageException(
						PLUGIN.getConfig().getTelegramSettings().getMessages().getMessage("not-your-account"));

			if (linkUser.get().getLinkUserInfo().getIdentificator().asNumber() != userId
					|| !PLUGIN.getConfig().getTelegramSettings().isAdministrator(userId))
				throw new SendMessageException(
						PLUGIN.getConfig().getTelegramSettings().getMessages().getMessage("not-your-account"));
			return account;
		});

		COMMAND_HANDLER.registerValueResolver(MessengerLinkContext.class, (context) -> {
			String code = context.popForParameter();
			TelegramActor commandActor = context.actor().as(TelegramActor.class);

			LinkConfirmationUser confirmationUser = Auth.getLinkConfirmationAuth()
					.getLinkUsers(linkUser -> linkUser.getLinkType().equals(TelegramLinkType.getInstance())
							&& linkUser.getLinkUserInfo().getIdentificator().asNumber() == commandActor.getId())
					.stream().findFirst().orElse(null);

			if (confirmationUser == null)
				throw new SendMessageException(
						PLUGIN.getConfig().getTelegramSettings().getMessages().getMessage("confirmation-no-code"));

			if (System.currentTimeMillis() > confirmationUser.getLinkTimeoutMillis())
				throw new SendMessageException(
						PLUGIN.getConfig().getTelegramSettings().getMessages().getMessage("confirmation-timed-out"));

			if (!confirmationUser.getConfirmationInfo().getConfirmationCode().equals(code))
				throw new SendMessageException(
						PLUGIN.getConfig().getTelegramSettings().getMessages().getMessage("confirmation-error"));

			LinkUserInfo telegramLinkUserInfo = confirmationUser.getAccount()
					.findFirstLinkUser(TelegramLinkType.LINK_USER_FILTER).get().getLinkUserInfo();

			if (telegramLinkUserInfo.getIdentificator().asNumber() != AccountFactory.DEFAULT_TELEGRAM_ID)
				throw new SendMessageException(PLUGIN.getConfig().getTelegramSettings().getMessages()
						.getMessage("confirmation-already-linked"));

			return new MessengerLinkContext(code, confirmationUser);
		});
		registerCommands();

		TELEGRAM_HOOK.getTelegramBot().setUpdatesListener(new TelegramCommandUpdatesListener());
	}
}
