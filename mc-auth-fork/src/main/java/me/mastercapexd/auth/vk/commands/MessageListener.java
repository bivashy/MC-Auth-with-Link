package me.mastercapexd.auth.vk.commands;

import com.ubivashka.lamp.commands.vk.core.BaseVkActor;
import com.ubivashka.lamp.commands.vk.core.VkHandler;
import com.ubivashka.vk.bungee.events.VKMessageEvent;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import revxrsal.commands.command.ArgumentStack;

public class MessageListener implements Listener {
	@EventHandler
	public void onMessage(VKMessageEvent event) {
		System.out.println("Dispatch");
		VkHandler.getInstances().forEach((commandHandler) -> {
			BaseVkActor baseVkActor = new BaseVkActor(event.getMessage(), commandHandler);
			ArgumentStack argumentStack = ArgumentStack.fromString(event.getMessage().getText());
			commandHandler.dispatch(baseVkActor, argumentStack);
		});
	}
}
