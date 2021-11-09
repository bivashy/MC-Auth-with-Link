package me.mastercapexd.auth.vk.commands;

import com.ubivashka.vk.bungee.events.VKMessageEvent;

import me.mastercapexd.auth.vk.commandhandler.VKCommandExecutor;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;

public class VKChangePasswordCommand extends VKCommandExecutor {
	private final VKReceptioner receptioner;

	public VKChangePasswordCommand(VKReceptioner receptioner) {
		this.receptioner = receptioner;
	}

	@Override
	public void execute(VKMessageEvent e, String[] args) {
		if (isChat(e.getPeer()))
			return;
		if (args.length < 2) {
			sendMessage(e.getPeer(),
					receptioner.getConfig().getVKMessages().getLegacyMessage("changepass-not-enough-arguments"));
			return;
		}
		String playerName = args[0];
		receptioner.actionWithAccount(e.getUserId(), playerName, account -> {
			String oldPassword = account.getPasswordHash();
			String newPassword = account.getHashType().hash(args[1]);
			if (oldPassword.equals(newPassword)) {
				sendMessage(e.getPeer(),
						receptioner.getConfig().getVKMessages().getLegacyMessage("changepass-nothing-to-change"));
				return;
			}

			if (args[1].length() < receptioner.getConfig().getPasswordMinLength()) {
				sendMessage(e.getPeer(),
						receptioner.getConfig().getVKMessages().getLegacyMessage("changepass-password-too-short"));
				return;
			}

			if (args[1].length() > receptioner.getConfig().getPasswordMaxLength()) {
				sendMessage(e.getPeer(),
						receptioner.getConfig().getVKMessages().getLegacyMessage("changepass-password-too-long"));
				return;
			}
			account.setPasswordHash(newPassword);
			sendMessage(e.getPeer(),
					receptioner.getConfig().getVKMessages().getMessage("changepass-success", e.getUserId(), account)
							.replaceAll("(?i)%password%", newPassword));
			receptioner.getAccountStorage().saveOrUpdateAccount(account);
		});
	}

	@Override
	public String getKey() {
		return "change-pass";
	}
}
