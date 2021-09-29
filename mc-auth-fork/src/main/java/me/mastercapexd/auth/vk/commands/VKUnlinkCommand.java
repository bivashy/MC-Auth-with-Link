package me.mastercapexd.auth.vk.commands;

import com.ubivashka.vk.bungee.events.VKMessageEvent;

import me.mastercapexd.auth.vk.accounts.VKLinkedAccount;
import me.mastercapexd.auth.vk.commandhandler.VKCommandExecutor;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;

public class VKUnlinkCommand extends VKCommandExecutor {
	private final VKReceptioner receptioner;

	public VKUnlinkCommand(VKReceptioner receptioner) {
		this.receptioner = receptioner;
	}

	@Override
	public void execute(VKMessageEvent e, String[] args) {
		if (isChat(e.getPeer()))
			return;
		if (args.length < 1) {
			sendMessage(e.getPeer(),
					receptioner.getConfig().getVKMessages().getLegacyMessage("unlink-not-enough-arguments"));
			return;
		}
		String playerName = args[0];
		receptioner.actionWithAccount(e.getUserId(), playerName, account -> {
			if (account == null)
				return;
			VKLinkedAccount linkedAccount = new VKLinkedAccount(receptioner, e.getUserId(), account);
			linkedAccount.unlink();
		});
	}

	@Override
	public String getKey() {
		return "unlink";
	}
}
