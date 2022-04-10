package me.mastercapexd.auth.messenger.commands;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.messenger.commands.parameters.MessengerLinkContext;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.storage.AccountStorage;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.orphan.OrphanCommand;

public class LinkCodeCommand implements OrphanCommand {
	@Dependency
	private PluginConfig config;
	@Dependency
	private AccountStorage accountStorage;

	@Default
	public void onLink(LinkCommandActorWrapper actorWrapper, LinkType linkType, MessengerLinkContext linkContext) {
		accountStorage.getAccount(linkContext.getConfirmationUser().getAccount().getId()).thenAccept(account -> {

			account.findFirstLinkUser(linkUser -> linkUser.getLinkType().getLinkName().equals(linkType.getLinkName()))
					.orElse(null).getLinkUserInfo().getIdentificator().setNumber(actorWrapper.userId());

			accountStorage.saveOrUpdateAccount(account);

			ProxyPlayer player = config.getActiveIdentifierType()
					.getPlayer(linkContext.getConfirmationUser().getAccount().getId());
			if (player != null)
				player.sendMessage(linkType.getProxyMessages().getStringMessage("linked"));

			actorWrapper.reply(linkType.getLinkMessages().getMessage("confirmation-success"));

			Auth.getLinkConfirmationAuth().removeLinkUser(linkContext.getConfirmationUser());

		});
	}
}
