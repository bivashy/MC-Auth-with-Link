package me.mastercapexd.auth.vk.buttonshandler;

import com.ubivashka.vk.bungee.events.VKCallbackButtonPressEvent;

public interface VKButtonExecutor {
	public void execute(VKCallbackButtonPressEvent event, String afterPayload);
}
