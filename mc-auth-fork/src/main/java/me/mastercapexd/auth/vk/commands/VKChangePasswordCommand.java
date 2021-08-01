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
		String newPassword = args[1];
		receptioner.actionWithAccount(e.getUserId(), playerName, account -> {
			
			account.setPasswordHash(account.getHashType().hash(newPassword));
			sendMessage(e.getPeer(), receptioner.getConfig().getVKMessages().getMessage("changepass-success", account)
					.replaceAll("(?i)%password%", newPassword));
			receptioner.getAccountStorage().saveOrUpdateAccount(account);
		});
	}
}
