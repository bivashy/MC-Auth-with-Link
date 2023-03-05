package me.mastercapexd.auth.velocity.hooks;

import com.bivashy.auth.api.AuthPlugin;
import com.ubivashka.vk.velocity.VelocityVkApiPlugin;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;

import me.mastercapexd.auth.hooks.VkPluginHook;

public class VelocityVkPluginHook implements VkPluginHook {
    @Override
    public boolean canHook() {
        return AuthPlugin.instance().getConfig().getVKSettings().isEnabled();
    }

    @Override
    public VkApiClient getClient() {
        return VelocityVkApiPlugin.getInstance().getVkApiProvider().getVkApiClient();
    }

    @Override
    public GroupActor getActor() {
        return VelocityVkApiPlugin.getInstance().getVkApiProvider().getActor();
    }
}
