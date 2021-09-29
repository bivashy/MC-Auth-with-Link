package me.mastercapexd.auth.vk.commandhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ubivashka.vk.bungee.events.VKMessageEvent;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class VKCommandHandler implements Listener {
	private ArrayList<VKCommand> commands = new ArrayList<>();

	public void addCommand(VKCommand command) {
		this.commands.add(command);
	}

	public void addCommand(String name, VKCommandExecutor executor) {
		this.commands.add(new VKCommand(name.toLowerCase(), executor));
	}

	public void removeCommand(VKCommand command) {
		commands.remove(command);
	}

	public ArrayList<VKCommand> getCommands() {
		return commands;
	}

	@EventHandler
	public void onVKMessage(VKMessageEvent e) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.execute(() -> {
			String message = e.getMessage().getText();
			if (message == null)
				return;
			if (message.isEmpty())
				return;
			String[] array = message.split("\\s+");
			String commandName = array[0];

			String[] args = Arrays.copyOfRange(array, 1, array.length);
			for (VKCommand cmd : this.commands) {
				if (cmd.isExecuted(commandName)) {
					cmd.execute(e, args);
					break;
				}
			}
		});
		executorService.shutdown();
	}
}
