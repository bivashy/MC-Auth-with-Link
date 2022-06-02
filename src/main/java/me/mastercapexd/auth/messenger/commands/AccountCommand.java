package me.mastercapexd.auth.messenger.commands;

import com.ubivaska.messenger.common.keyboard.Keyboard;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.LinkType;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.orphan.OrphanCommand;

public class AccountCommand implements OrphanCommand {
	@Default
	public void accountMenu(LinkCommandActorWrapper actorWrapper, LinkType linkType, Account account) {
		Keyboard accountKeyboard = linkType.getSettings().getKeyboards().createKeyboard("account", "%account_name%",
				account.getName());
		actorWrapper.send(linkType.newMessageBuilder(linkType.getLinkMessages().getMessage("account-control", linkType.newMessageContext(account)))
				.keyboard(accountKeyboard).build());
	}
}
