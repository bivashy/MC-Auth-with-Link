package me.mastercapexd.auth.messenger.commands;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.link.message.keyboard.IKeyboard;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.orphan.OrphanCommand;

public class AccountCommand implements OrphanCommand {
	@Default
	public void accountMenu(LinkCommandActorWrapper actorWrapper,LinkType linkType,Account account) {
		IKeyboard accountKeyboard = linkType.getSettings().getKeyboards().createKeyboard("account", "%account_name%",account.getName());
		actorWrapper.send(linkType.newMessageBuilder().keyboard(accountKeyboard).rawContent(linkType.getLinkMessages().getMessage("account-control")).build());
	}
}
