package me.mastercapexd.auth.messenger.commands;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.messenger.commands.annotations.ConfigurationArgumentError;
import me.mastercapexd.auth.proxy.commands.parameters.NewPassword;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.orphan.OrphanCommand;

public class ChangePasswordCommand implements OrphanCommand {
	@Default
	@ConfigurationArgumentError("changepass-not-enough-arguments")
	public void onPasswordChange(LinkCommandActorWrapper actorWrapper, LinkType linkType, Account player,
			NewPassword newPassword) {
		player.setPasswordHash(player.getHashType().hash(newPassword.getNewPassword()));
		actorWrapper.reply(linkType.getLinkMessages().getStringMessage("changepass-success")
				.replaceAll("(?i)%password%", newPassword.getNewPassword()));

	}
}
