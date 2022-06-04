package me.mastercapexd.auth.messenger.commands;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.link.user.info.confirmation.LinkUserConfirmationState;
import me.mastercapexd.auth.messenger.commands.annotations.ConfigurationArgumentError;
import me.mastercapexd.auth.storage.AccountStorage;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.orphan.OrphanCommand;

public class ConfirmationToggleCommand implements OrphanCommand {
	@Dependency
	private AccountStorage accountStorage;

	@Default
	@ConfigurationArgumentError("confirmation-no-player")
	public void onKick(LinkCommandActorWrapper actorWrapper, LinkType linkType, Account account) {
		if (!linkType.getSettings().getConfirmationSettings().canToggleConfirmation()) {
			actorWrapper.reply(linkType.getLinkMessages().getMessage("confirmation-toggle-disabled",
					linkType.newMessageContext(account)));
			return;
		}
		actorWrapper.reply(
				linkType.getLinkMessages().getMessage("confirmation-toggled", linkType.newMessageContext(account)));
		LinkUserConfirmationState confirmationState = account
				.findFirstLinkUser(user -> user.getLinkType().equals(linkType)).get().getLinkUserInfo()
				.getConfirmationState();
		confirmationState.setSendConfirmation(!confirmationState.shouldSendConfirmation());
		accountStorage.saveOrUpdateAccount(account);
	}
}
