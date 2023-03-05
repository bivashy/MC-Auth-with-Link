package me.mastercapexd.auth.bungee.hooks;

import com.bivashy.auth.api.AuthPlugin;
import com.ubivashka.vk.bungee.BungeeVkApiPlugin;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;

import me.mastercapexd.auth.hooks.VkPluginHook;

public class BungeeVkPluginHook implements VkPluginHook {
    @Override
    public boolean canHook() {
        return AuthPlugin.instance().getConfig().getVKSettings().isEnabled();
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
