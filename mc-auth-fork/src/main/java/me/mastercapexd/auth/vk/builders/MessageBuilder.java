package me.mastercapexd.auth.vk.builders;

import java.util.Random;

import com.google.gson.Gson;
import com.ubivashka.vk.bungee.VKAPI;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;

public abstract class MessageBuilder {
	public static final VkApiClient vk = VKAPI.getInstance().getVK();
	public static final GroupActor actor = VKAPI.getInstance().getActor();
	public static final Random random = new Random();
	public static final Gson gson = new Gson();

	public abstract MessagesSendQuery build();

	public boolean execute() {
		try {
			build().execute();
			return true;
		} catch (ApiException | ClientException e) {
			e.printStackTrace();
			return false;
		}
	}
}
