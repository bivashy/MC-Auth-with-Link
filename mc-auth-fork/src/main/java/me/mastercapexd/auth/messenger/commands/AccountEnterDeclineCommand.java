package me.mastercapexd.auth.messenger.commands;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Predicate;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.link.entryuser.LinkEntryUser;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.orphan.OrphanCommand;

public class AccountEnterDeclineCommand implements OrphanCommand {
	@Dependency
	private PluginConfig config;

	@Default
	public void onDecline(LinkCommandActorWrapper actorWrapper, LinkType linkType,
			@Default("all") String acceptPlayerName) {
		Predicate<LinkEntryUser> filter = entryUser -> {
			if (!entryUser.getLinkType().equals(linkType))
				return false;

			if (!entryUser.getLinkUserInfo().getIdentificator().equals(actorWrapper.userId()))
				return false;

			Duration confirmationSecondsPassed = Duration
					.of(System.currentTimeMillis() - entryUser.getConfirmationStartTime(), ChronoUnit.MILLIS);

			if (confirmationSecondsPassed.getSeconds() > config.getVKSettings().getEnterSettings().getEnterDelay()) // If enter delay was passed
				return false;

			if (!acceptPlayerName.equals("all")) // If player not default value
				return entryUser.getAccount().getName().equalsIgnoreCase(acceptPlayerName); // Check if entryUser name
																							// equals to accept player
			return true;
		};

		List<LinkEntryUser> accounts = Auth.getLinkEntryAuth().getLinkUsers(filter);
		if (accounts.isEmpty()) {
			actorWrapper.reply(linkType.getLinkMessages().getMessage("enter-no-accounts"));
			return;
		}
		accounts.forEach((entryUser) -> {
			entryUser.getAccount().kick(linkType.getLinkMessages().getStringMessage("enter-declined"));
		});

		actorWrapper.reply(linkType.getLinkMessages().getMessage("enter-declined"));
	}
}
