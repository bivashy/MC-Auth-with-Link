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
import com.ubivashka.messenger.vk.message.keyboard.VkKeyboard;
import com.ubivashka.vk.api.parsers.objects.CallbackButtonEvent;
import com.ubivaska.messenger.common.identificator.Identificator;
import com.ubivaska.messenger.common.message.Message;
import com.ubivaska.messenger.common.message.Message.MessageBuilder;
import com.vk.api.sdk.objects.messages.Keyboard;

import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.link.vk.VKCommandActorWrapper;
import me.mastercapexd.auth.link.vk.VKLinkType;
import me.mastercapexd.auth.messenger.commands.custom.CustomCommandExecuteContext;
import me.mastercapexd.auth.messenger.commands.custom.MessengerCustomCommand;
import revxrsal.commands.command.ArgumentStack;
import revxrsal.commands.command.CommandActor;

public abstract class DispatchCommandListener {
	private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
	private static final LinkType LINK_TYPE = VKLinkType.getInstance();
	private static final Gson GSON = new Gson();

	protected void onMessage(com.vk.api.sdk.objects.messages.Message vkMessage, int peerId) {
		EXECUTOR_SERVICE.execute(() -> {
			VkHandler.getInstances().forEach((commandHandler) -> {
				handleCommand(commandHandler, new MessageDispatchSource(vkMessage));

				LINK_TYPE.getSettings().getCustomCommands()
						.execute(new CustomCommandExecuteContext(vkMessage.getText())).forEach(customCommand -> {
							Message message = createMessage(customCommand);
							message.send(Identificator.of(peerId));
						});
			});
		});
	}

	protected void onButtonClick(CallbackButtonEvent buttonEvent) {
		EXECUTOR_SERVICE.execute(() -> {
			VkHandler.getInstances().forEach((commandHandler) -> {
				CallbackButton callbackButton = GSON.fromJson(GSON.toJson(buttonEvent), CallbackButton.class);
				handleCommand(commandHandler, new ButtonDispatchSource(callbackButton));

				LINK_TYPE.getSettings().getCustomCommands()
						.execute(new CustomCommandExecuteContext(buttonEvent.getPayload()).setButtonExecution(true))
						.forEach(customCommand -> {
							Message message = createMessage(customCommand);
							message.send(Identificator.of(buttonEvent.getPeerID()));
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
			builder.keyboard(new VkKeyboard(
					GSON.fromJson(customCommand.getSectionHolder().getString("keyboard"), Keyboard.class)));
		return builder.build();
	}
}
