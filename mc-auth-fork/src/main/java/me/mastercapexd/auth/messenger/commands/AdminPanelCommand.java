package me.mastercapexd.auth.messenger.commands;

import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.link.message.keyboard.IKeyboard;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.orphan.OrphanCommand;

public class AdminPanelCommand implements OrphanCommand {
	@Default
	public void adminPanelMenu(LinkCommandActorWrapper actorWrapper, LinkType linkType) {
		IKeyboard adminPanelKeyboard = linkType.getSettings().getKeyboards().createKeyboard("admin-panel");
		actorWrapper.send(linkType.newMessageBuilder(linkType.getLinkMessages().getMessage("admin-panel"))
				.keyboard(adminPanelKeyboard).build());
	}
}
