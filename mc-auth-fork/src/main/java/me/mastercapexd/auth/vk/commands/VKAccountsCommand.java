package me.mastercapexd.auth.vk.commands;

import com.ubivashka.vk.bungee.events.VKMessageEvent;

import me.mastercapexd.auth.vk.VKAccountsPageType;
import me.mastercapexd.auth.vk.builders.AccountsMessageBuilder;
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
		receptioner.getAccountStorage().getAccountsByVKID(e.getUserId()).thenAccept(accounts -> {
			new AccountsMessageBuilder(e.getUserId(), 1, VKAccountsPageType.OWNPAGE, accounts, receptioner).execute();
		});
	}

	@Override
	public String getKey() {
		return "accounts";
	}

}
