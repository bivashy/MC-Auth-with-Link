package me.mastercapexd.auth.messenger.commands;

import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.LinkType;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.orphan.OrphanCommand;

public class AccountEnterAcceptCommand implements OrphanCommand {

	@Default
	public void onAccept(LinkCommandActorWrapper actorWrapper, LinkType linkType) {
		
	}
	
}
