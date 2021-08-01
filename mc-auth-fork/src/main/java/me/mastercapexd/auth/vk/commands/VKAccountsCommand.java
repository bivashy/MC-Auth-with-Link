package me.mastercapexd.auth.vk.commands;

import com.ubivashka.vk.bungee.events.VKMessageEvent;

import me.mastercapexd.auth.vk.commandhandler.VKCommandExecutor;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;

public class VKAccountsCommand extends VKCommandExecutor {

	private final VKReceptioner receptioner;

	public VKAccountsCommand(VKReceptioner receptioner) {
		this.receptioner = receptioner;
	}

	@Override
	public void execute(VKMessageEvent e, String[] args) {
		if (isChat(e.getPeer()))
			return;
		receptioner.getPlugin().getVkUtils().sendAccountsKeyboard(e.getUserId(), 1);
	}

}
