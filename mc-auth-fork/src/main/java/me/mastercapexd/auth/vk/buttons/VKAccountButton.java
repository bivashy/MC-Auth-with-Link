package me.mastercapexd.auth.vk.buttons;

import com.ubivashka.vk.bungee.events.VKCallbackButtonPressEvent;

import me.mastercapexd.auth.vk.accounts.VKLinkedAccount;
import me.mastercapexd.auth.vk.buttonshandler.VKButtonExecutor;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;

public class VKAccountButton implements VKButtonExecutor {
	private final VKReceptioner receptioner;

	public VKAccountButton(VKReceptioner receptioner) {
		this.receptioner = receptioner;
	}

	@Override
	public void execute(VKCallbackButtonPressEvent e, String id) {
		receptioner.getAccountStorage().getAccount(id).thenAccept(account -> {
			VKLinkedAccount linkedAccount = new VKLinkedAccount(receptioner, e.getButtonEvent().getUserID(), account);
			linkedAccount.sendAccountSettingsKeyboard();
		});

	}

}
