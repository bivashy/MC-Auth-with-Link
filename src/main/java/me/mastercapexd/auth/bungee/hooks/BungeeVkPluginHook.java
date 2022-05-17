package me.mastercapexd.auth.bungee.hooks;

import com.ubivashka.vk.api.providers.VkApiProvider;
import com.ubivashka.vk.bungee.BungeeVkApiPlugin;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;

import me.mastercapexd.auth.hooks.VkPluginHook;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class BungeeVkPluginHook implements VkPluginHook {
	private static final VkApiProvider VK_API_PROVIDER = BungeeVkApiPlugin.getInstance().getVkApiProvider();

	@Override
	public boolean canHook() {
		return ProxyPlugin.instance().getConfig().getVKSettings().isEnabled();
	}

	@Override
	public VkApiClient getClient() {
		return VK_API_PROVIDER.getVkApiClient();
	}

	@Override
	public GroupActor getActor() {
		return VK_API_PROVIDER.getActor();
	}
}
