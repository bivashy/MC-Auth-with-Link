package me.mastercapexd.auth.bungee.listeners;

import com.ubivashka.vk.bungee.events.VKCallbackButtonPressEvent;
import com.ubivashka.vk.bungee.events.VKMessageEvent;

import me.mastercapexd.auth.vk.commands.DispatchCommandListener;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class VkDispatchListener extends DispatchCommandListener implements Listener {
    @EventHandler
    public void onMessage(VKMessageEvent event) {
        onMessage(event.getMessage(), event.getPeer());
    }

    @EventHandler
    public void onButtonPress(VKCallbackButtonPressEvent event) {
        onButtonClick(event.getButtonEvent());
    }
}
