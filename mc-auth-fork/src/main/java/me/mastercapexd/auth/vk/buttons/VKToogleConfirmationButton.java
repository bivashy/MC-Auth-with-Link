package me.mastercapexd.auth.vk.buttons;

import java.util.HashMap;
import java.util.Map;

import com.ubivashka.vk.bungee.events.VKCallbackButtonPressEvent;
import com.ubivashka.vk.callback.objects.CallbackButtonEvent;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

import me.mastercapexd.auth.vk.buttonshandler.VKButtonExecutor;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;

public class VKToogleConfirmationButton implements VKButtonExecutor {
	private final VKReceptioner receptioner;

	public VKToogleConfirmationButton(VKReceptioner receptioner) {
		this.receptioner = receptioner;
	}

	@Override
	public void execute(VKCallbackButtonPressEvent e, String id) {
		if (!receptioner.getConfig().getVKSettings().getEnterSettings().canToggleEnterConfirmation())
			return;
		receptioner.getAccountStorage().getAccount(id).thenAccept(account -> {
			account.setVkConfirmationEnabled(!account.isVKConfirmationEnabled());
			CallbackButtonEvent buttonEvent = e.getButtonEvent();
			Map<String, String> myMap = new HashMap<String, String>();
			myMap.put("type", "show_snackbar");
			if (account.isVKConfirmationEnabled()) {
				myMap.put("text", receptioner.getConfig().getVKMessages().getLegacyMessage("enter-enabled"));
				String json = GSON.toJson(myMap);
				try {
					VK.messages().sendMessageEventAnswer(ACTOR, buttonEvent.getEventID(), buttonEvent.getUserID(),
							buttonEvent.getPeerID()).eventData(json).execute();
				} catch (ApiException | ClientException e1) {
					e1.printStackTrace();
				}
			} else {
				myMap.put("text", receptioner.getConfig().getVKMessages().getLegacyMessage("enter-disabled"));
				String json = GSON.toJson(myMap);
				try {
					VK.messages().sendMessageEventAnswer(ACTOR, buttonEvent.getEventID(), buttonEvent.getUserID(),
							buttonEvent.getPeerID()).eventData(json).execute();
				} catch (ApiException | ClientException e1) {
					e1.printStackTrace();
				}
			}
			receptioner.getAccountStorage().saveOrUpdateAccount(account);
		});

	}

}
