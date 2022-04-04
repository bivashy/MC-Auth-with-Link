package me.mastercapexd.auth.vk.buttonshandler;

import com.google.gson.Gson;
import com.ubivashka.vk.bungee.VKAPI;
import com.ubivashka.vk.bungee.events.VKCallbackButtonPressEvent;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;

public interface VKButtonExecutor {
	public static final VkApiClient VK = VKAPI.getInstance().getVK();
	public static final GroupActor ACTOR = VKAPI.getInstance().getActor();
	public static final Gson GSON = new Gson();

	void execute(VKCallbackButtonPressEvent event, String afterPayload);
}
