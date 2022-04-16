package me.mastercapexd.auth.messenger.commands;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.account.factories.AccountFactory;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.storage.AccountStorage;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.orphan.OrphanCommand;

public class UnlinkCommand implements OrphanCommand {
	@Dependency
	private AccountStorage accountStorage;

	@Default
	public void onAccept(LinkCommandActorWrapper actorWrapper, LinkType linkType, Account player) {
		player.findFirstLinkUser(user -> user.getLinkType().equals(linkType)).get().getLinkUserInfo().getIdentificator()
				.setNumber(AccountFactory.DEFAULT_VK_ID);
		accountStorage.saveOrUpdateAccount(player);
	}
}
