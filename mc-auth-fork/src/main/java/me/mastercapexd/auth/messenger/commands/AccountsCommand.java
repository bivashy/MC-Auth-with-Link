package me.mastercapexd.auth.messenger.commands;

import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.storage.AccountStorage;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Flag;
import revxrsal.commands.orphan.OrphanCommand;

public class AccountsCommand implements OrphanCommand {
	@Dependency
	private AccountStorage accountStorage;

	@Default
	public void onAccountsMenu(LinkCommandActorWrapper actorWrapper, LinkType linkType,
			@Default("1") Integer accountsPage, @Flag("type") @Default("my") String type) {
	}
}
