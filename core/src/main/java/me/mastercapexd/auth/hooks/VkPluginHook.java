package me.mastercapexd.auth.hooks;

import com.bivashy.auth.api.hook.PluginHook;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;

public interface VkPluginHook extends PluginHook {
    VkApiClient getClient();

    GroupActor getActor();
}
