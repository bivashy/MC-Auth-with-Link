package me.mastercapexd.auth.vk.commands;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.ubivashka.lamp.commands.vk.VkCommandHandler;
import com.ubivashka.lamp.commands.vk.core.BaseVkActor;
import com.ubivashka.lamp.commands.vk.core.VkHandler;
import com.ubivashka.lamp.commands.vk.message.ButtonDispatchSource;
import com.ubivashka.lamp.commands.vk.message.DispatchSource;
import com.ubivashka.lamp.commands.vk.message.MessageDispatchSource;
import com.ubivashka.lamp.commands.vk.objects.CallbackButton;
import com.ubivashka.vk.bungee.events.VKCallbackButtonPressEvent;
import com.ubivashka.vk.bungee.events.VKMessageEvent;
import com.vk.api.sdk.objects.messages.Keyboard;

import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.link.message.Message;
import me.mastercapexd.auth.link.message.Message.MessageBuilder;
import me.mastercapexd.auth.link.message.vk.VKKeyboard;
import me.mastercapexd.auth.link.vk.VKCommandActorWrapper;
import me.mastercapexd.auth.link.vk.VKLinkType;
import me.mastercapexd.auth.messenger.commands.custom.CustomCommandExecuteContext;
import me.mastercapexd.auth.messenger.commands.custom.MessengerCustomCommand;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import revxrsal.commands.command.ArgumentStack;
import revxrsal.commands.command.CommandActor;

public class DispatchCommandListener implements Listener {
	private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
	private static final LinkType LINK_TYPE = VKLinkType.getInstance();
	private static final Gson GSON = new Gson();

	@EventHandler
	public void onMessage(VKMessageEvent event) {
		EXECUTOR_SERVICE.execute(() -> {
			VkHandler.getInstances().forEach((commandHandler) -> {
				handleCommand(commandHandler, new MessageDispatchSource(event.getMessage()));

				LINK_TYPE.getSettings().getCustomCommands()
						.execute(new CustomCommandExecuteContext(event.getMessage().getText()))
						.forEach(customCommand -> {
							Message message = createMessage(customCommand);
							message.send(event.getPeer());
						});
			});
		});
	}

	@EventHandler
	public void onButtonClick(VKCallbackButtonPressEvent event) {
		EXECUTOR_SERVICE.execute(() -> {
			VkHandler.getInstances().forEach((commandHandler) -> {
				CallbackButton callbackButton = GSON.fromJson(GSON.toJson(event.getButtonEvent()),
						CallbackButton.class);
				handleCommand(commandHandler, new ButtonDispatchSource(callbackButton));

				LINK_TYPE.getSettings().getCustomCommands().execute(
						new CustomCommandExecuteContext(event.getButtonEvent().getPayload()).setButtonExecution(true))
						.forEach(customCommand -> {
							Message message = createMessage(customCommand);
							message.send(event.getButtonEvent().getPeerID());
						});
			});
		});
	}

	private void handleCommand(VkCommandHandler handler, DispatchSource source) {
		CommandActor commandActor = new VKCommandActorWrapper(new BaseVkActor(source, handler));
		ArgumentStack argumentStack = source.getArgumentStack(handler);
		handler.dispatch(commandActor, argumentStack);
	}

	private Message createMessage(MessengerCustomCommand customCommand) {
		MessageBuilder builder = LINK_TYPE.newMessageBuilder(customCommand.getAnswer());
		if (customCommand.getSectionHolder().contains("keyboard"))
			builder.keyboard(new VKKeyboard(
					GSON.fromJson(customCommand.getSectionHolder().getString("keyboard"), Keyboard.class)));
		return builder.build();
	}
}
