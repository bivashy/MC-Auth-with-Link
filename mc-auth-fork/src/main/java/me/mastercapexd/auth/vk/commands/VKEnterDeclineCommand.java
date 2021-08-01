package me.mastercapexd.auth.vk.commands;

import java.util.List;

import com.ubivashka.vk.bungee.events.VKMessageEvent;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.VKEnterAnswer;
import me.mastercapexd.auth.vk.accounts.VKEntryAccount;
import me.mastercapexd.auth.vk.commandhandler.VKCommandExecutor;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;

public class VKEnterDeclineCommand extends VKCommandExecutor {
	private final VKReceptioner receptioner;

	public VKEnterDeclineCommand(VKReceptioner receptioner) {
		this.receptioner = receptioner;
	}

	@Override
	public void execute(VKMessageEvent e, String[] args) {
		if (isChat(e.getPeer()))
			return;
		List<VKEntryAccount> accounts = Auth.getEntryAccount(e.getUserId(),
				receptioner.getConfig().getVKSettings().getEnterSettings().getEnterDelay());
		if (accounts.isEmpty())
			sendMessage(e.getPeer(), receptioner.getConfig().getVKMessages().getLegacyMessage("enter-no-enter"));
		for (VKEntryAccount account : accounts)
			account.enterConnect(VKEnterAnswer.DECLINE, receptioner.getConfig(), receptioner.getAccountStorage());
	}

}
