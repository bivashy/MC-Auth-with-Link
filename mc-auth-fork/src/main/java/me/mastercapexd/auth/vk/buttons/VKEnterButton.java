package me.mastercapexd.auth.vk.buttons;

import com.ubivashka.vk.bungee.events.VKCallbackButtonPressEvent;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.VKEnterAnswer;
import me.mastercapexd.auth.vk.accounts.VKEntryAccount;
import me.mastercapexd.auth.vk.buttonshandler.VKButtonExecutor;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;

public class VKEnterButton implements VKButtonExecutor {
	private final VKReceptioner receptioner;

	public VKEnterButton(VKReceptioner receptioner) {
		this.receptioner = receptioner;
	}

	@Override
	public void execute(VKCallbackButtonPressEvent e, String payload) {
		if (!Auth.hasEntryAccount(e.getButtonEvent().getUserID(),
				receptioner.getConfig().getVKSettings().getEnterSettings().getEnterDelay()))
			return;
		VKEnterAnswer answer = VKEnterAnswer.getByPayload(payload.split("_")[0]);
		String buttonUUID = payload.split("_")[1];
		VKEntryAccount account = Auth.getEntryAccountByButtonUUID(buttonUUID);
		if (account == null)
			return;
		account.enterConnect(answer, receptioner.getConfig(), receptioner.getAccountStorage());

	}

}
