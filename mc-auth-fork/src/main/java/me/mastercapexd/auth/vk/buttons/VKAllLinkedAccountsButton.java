package me.mastercapexd.auth.vk.buttons;

import com.ubivashka.vk.bungee.events.VKCallbackButtonPressEvent;

import me.mastercapexd.auth.vk.VKAccountsPageType;
import me.mastercapexd.auth.vk.builders.AccountsMessageBuilder;
import me.mastercapexd.auth.vk.buttonshandler.VKButtonExecutor;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;

public class VKAllLinkedAccountsButton implements VKButtonExecutor {
	private final VKReceptioner receptioner;

	public VKAllLinkedAccountsButton(VKReceptioner receptioner) {
		this.receptioner = receptioner;
	}

	@Override
	public void execute(VKCallbackButtonPressEvent e, String payload) {
		if (!receptioner.getConfig().getVKSettings().isAdministrator(e.getButtonEvent().getUserID()))
			return;
		receptioner.getAccountStorage().getAllLinkedAccounts().thenAccept(accounts -> {
			new AccountsMessageBuilder(e.getButtonEvent().getUserID(), 1, VKAccountsPageType.ALLLINKEDACCOUNTSPAGE,
					accounts, receptioner).execute();
		});

	}
}
