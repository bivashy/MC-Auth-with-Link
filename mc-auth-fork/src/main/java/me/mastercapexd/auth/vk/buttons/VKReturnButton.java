package me.mastercapexd.auth.vk.buttons;

import com.ubivashka.vk.bungee.events.VKCallbackButtonPressEvent;

import me.mastercapexd.auth.vk.VKAccountsPageType;
import me.mastercapexd.auth.vk.builders.AccountsMessageBuilder;
import me.mastercapexd.auth.vk.buttonshandler.VKButtonExecutor;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;

public class VKReturnButton implements VKButtonExecutor {
	private final VKReceptioner receptioner;

	public VKReturnButton(VKReceptioner receptioner) {
		this.receptioner = receptioner;
	}

	@Override
	public void execute(VKCallbackButtonPressEvent e, String id) {
		receptioner.getAccountStorage().getAccountsByVKID(e.getButtonEvent().getUserID()).thenAccept(accounts -> {
			new AccountsMessageBuilder(e.getButtonEvent().getUserID(), 1, VKAccountsPageType.OWNPAGE, accounts,
					receptioner).execute();
		});

	}
}