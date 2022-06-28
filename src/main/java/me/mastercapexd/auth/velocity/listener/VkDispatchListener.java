package me.mastercapexd.auth.velocity.listener;

import com.ubivashka.vk.velocity.events.VKCallbackButtonPressEvent;
import com.ubivashka.vk.velocity.events.VKMessageEvent;
import com.velocitypowered.api.event.Subscribe;

import me.mastercapexd.auth.vk.commands.DispatchCommandListener;

public class VkDispatchListener extends DispatchCommandListener {
    @Subscribe
    public void onMessage(VKMessageEvent event) {
        onMessage(event.getMessage(), event.getPeer());
    }

    @Subscribe
    public void onButtonPress(VKCallbackButtonPressEvent event) {
        onButtonClick(event.getButtonEvent());
    }
}
