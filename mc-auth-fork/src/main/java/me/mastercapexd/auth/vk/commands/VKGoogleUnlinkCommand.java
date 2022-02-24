package me.mastercapexd.auth.vk.commands;

import com.ubivashka.vk.bungee.events.VKMessageEvent;

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
			sendMessage(e.getPeer(), receptioner.getConfig().getVKSettings().getVKMessages().getMessage("google-disabled"));
			return;
		}
		if (args.length == 0) {
			sendMessage(e.getPeer(),
					receptioner.getConfig().getVKSettings().getVKMessages().getMessage("google-unlink-not-enough-arguments"));
			return;
		}
		String playerName = args[0];
		receptioner.actionWithAccount(e.getUserId(), playerName, account -> {
			if (account.getGoogleKey() == null || account.getGoogleKey().isEmpty()) {
				sendMessage(e.getPeer(),
						receptioner.getConfig().getVKSettings().getVKMessages().getMessage("google-unlink-not-have-google"));
				return;
			}
			account.setGoogleKey(null);
			receptioner.getAccountStorage().saveOrUpdateAccount(account);
			sendMessage(e.getPeer(), receptioner.getConfig().getVKSettings().getVKMessages().getMessage("google-unlinked"));
		});
	}

	@Override
	public String getKey() {
		return "google-remove";
	}

}
