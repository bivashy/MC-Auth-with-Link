package me.mastercapexd.auth.vk.commands;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ubivashka.lamp.commands.vk.core.BaseVkActor;
import com.ubivashka.lamp.commands.vk.core.VkHandler;
import com.ubivashka.vk.bungee.events.VKMessageEvent;

import me.mastercapexd.auth.link.vk.VKCommandActorWrapper;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import revxrsal.commands.command.ArgumentStack;
import revxrsal.commands.command.CommandActor;

public class MessageListener implements Listener {
	private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

	@EventHandler
	public void onMessage(VKMessageEvent event) {
		EXECUTOR_SERVICE.execute(() -> {
			VkHandler.getInstances().forEach((commandHandler) -> {
				CommandActor commandActor = new VKCommandActorWrapper(
						new BaseVkActor(event.getMessage(), commandHandler));
				ArgumentStack argumentStack = ArgumentStack.fromString(event.getMessage().getText());
				commandHandler.dispatch(commandActor, argumentStack);
			});
		});
	}
}
