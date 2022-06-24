package me.mastercapexd.auth.bungee.hooks;

import com.ubivashka.vk.bungee.BungeeVkApiPlugin;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;

import me.mastercapexd.auth.hooks.VkPluginHook;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class BungeeVkPluginHook implements VkPluginHook {
    @Override
    public boolean canHook() {
        return ProxyPlugin.instance().getConfig().getVKSettings().isEnabled();
    }

    @Override
    public VkApiClient getClient() {
        return BungeeVkApiPlugin.getInstance().getVkApiProvider().getVkApiClient();
    }

    @Override
    public GroupActor getActor() {
        return BungeeVkApiPlugin.getInstance().getVkApiProvider().getActor();
    }
}
