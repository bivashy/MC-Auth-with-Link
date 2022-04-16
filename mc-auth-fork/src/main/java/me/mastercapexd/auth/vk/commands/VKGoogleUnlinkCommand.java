package me.mastercapexd.auth.vk.commands;

import com.ubivashka.vk.bungee.events.VKMessageEvent;

import me.mastercapexd.auth.link.google.GoogleLinkType;
import me.mastercapexd.auth.link.user.LinkUser;
import me.mastercapexd.auth.vk.commandhandler.VKCommandExecutor;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;

public class VKGoogleUnlinkCommand extends VKCommandExecutor {
	private final VKReceptioner receptioner;

	public VKGoogleUnlinkCommand(VKReceptioner receptioner) {
		this.receptioner = receptioner;
	}

	@Override
	public void execute(VKMessageEvent e, String[] args) {
		if (!receptioner.getConfig().getGoogleAuthenticatorSettings().isEnabled()) {
			sendMessage(e.getPeer(),
					receptioner.getConfig().getVKSettings().getMessages().getMessage("google-disabled"));
			return;
		}
		if (args.length == 0) {
			sendMessage(e.getPeer(), receptioner.getConfig().getVKSettings().getMessages()
					.getMessage("google-unlink-not-enough-arguments"));
			return;
		}
		String playerName = args[0];
		receptioner.actionWithAccount(e.getUserId(), playerName, account -> {
			LinkUser linkUser = account.findFirstLinkUser(GoogleLinkType.LINK_USER_FILTER).orElse(null);
			if (linkUser == null || linkUser.getLinkUserInfo().getIdentificator().asString().isEmpty()) {
				sendMessage(e.getPeer(), receptioner.getConfig().getVKSettings().getMessages()
						.getMessage("google-unlink-not-have-google"));
				return;
			}
			linkUser.getLinkUserInfo().getIdentificator().setString(GoogleLinkType.NULL_KEY);
			receptioner.getAccountStorage().saveOrUpdateAccount(account);
			sendMessage(e.getPeer(),
					receptioner.getConfig().getVKSettings().getMessages().getMessage("google-unlinked"));
		});
	}

	@Override
	public String getKey() {
		return "google-remove";
	}

}
