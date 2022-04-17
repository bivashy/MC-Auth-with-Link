package me.mastercapexd.auth.messenger.commands;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.storage.AccountStorage;
import me.mastercapexd.auth.utils.RandomCodeFactory;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.orphan.OrphanCommand;

public class RestoreCommand implements OrphanCommand {
	@Dependency
	private PluginConfig config;
	@Dependency
	private AccountStorage accountStorage;

	@Default
	public void onRestore(LinkCommandActorWrapper actorWrapper, LinkType linkType, Account player) {
		String generatedPassword = RandomCodeFactory
				.generateCode(linkType.getSettings().getRestoreSettings().getCodeLength());
		player.setPasswordHash(player.getHashType().hash(generatedPassword));
		player.logout(config.getSessionDurability());
		player.kick(linkType.getProxyMessages().getStringMessage("kicked"));
		actorWrapper.reply(
				linkType.getLinkMessages().getMessage("restored").replaceAll("(?i)%password%", generatedPassword));
		accountStorage.saveOrUpdateAccount(player);
	}
}
