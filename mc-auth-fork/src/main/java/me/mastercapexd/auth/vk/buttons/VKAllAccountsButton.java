package me.mastercapexd.auth.vk.buttons;

import com.ubivashka.vk.bungee.events.VKCallbackButtonPressEvent;

import me.mastercapexd.auth.vk.VKAccountsPageType;
import me.mastercapexd.auth.vk.builders.AccountsMessageBuilder;
import me.mastercapexd.auth.vk.buttonshandler.VKButtonExecutor;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;

public class VKAllAccountsButton implements VKButtonExecutor {
	private final VKReceptioner receptioner;

	public VKAllAccountsButton(VKReceptioner receptioner) {
		this.receptioner = receptioner;
	}

	@Override
	public void execute(VKCallbackButtonPressEvent e, String payload) {
		if (!receptioner.getConfig().getVKSettings().isAdminUser(e.getButtonEvent().getUserID()))
			return;
		receptioner.getAccountStorage().getAllAccounts().thenAccept(accounts -> {
			new AccountsMessageBuilder(e.getButtonEvent().getUserID(), 1, VKAccountsPageType.ALLACCOUNTSPAGE, accounts,
					receptioner).execute();
		});

	}
}
