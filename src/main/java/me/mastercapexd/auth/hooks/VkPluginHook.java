package me.mastercapexd.auth.hooks;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;

import me.mastercapexd.auth.proxy.hooks.PluginHook;

public interface VkPluginHook extends PluginHook {
	VkApiClient getClient();

	GroupActor getActor();
}
