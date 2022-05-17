package me.mastercapexd.auth.messenger.commands;

import me.mastercapexd.auth.KickResult;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.messenger.commands.annotations.ConfigurationArgumentError;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.orphan.OrphanCommand;

public class KickCommand implements OrphanCommand {
	@Default
	@ConfigurationArgumentError("kick-not-enough-arguments")
	public void onKick(LinkCommandActorWrapper actorWrapper, LinkType linkType, Account player) {
		actorWrapper.reply(linkType.getLinkMessages().getMessage("kick-starting"));
		KickResult kickResult = player.kick(linkType.getProxyMessages().getStringMessage("kicked"));
		actorWrapper.reply(linkType.getLinkMessages().getMessage(kickResult.getConfigurationPath()));
	}
}
