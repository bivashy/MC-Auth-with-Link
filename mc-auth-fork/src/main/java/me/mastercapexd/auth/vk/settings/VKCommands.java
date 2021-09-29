package me.mastercapexd.auth.vk.settings;

import java.util.HashMap;

import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;
import me.mastercapexd.auth.vk.commands.VKCustomCommand;
import net.md_5.bungee.config.Configuration;

public class VKCommands {
	private final HashMap<String, VKCustomCommand> commands = new HashMap<>();

	public VKCommands(Configuration section) {
		for (String command : section.getKeys())
			addCommand(command, section.getSection(command));
	}

	private void addCommand(String command, Configuration section) {
		VKCustomCommand vkCommand = new VKCustomCommand(command, section);
		commands.put(vkCommand.getCommand(), vkCommand);
	}

	public void registerCommands(VKReceptioner receptioner) {
		for (String command : commands.keySet()) {
			VKCustomCommand customCommand = commands.get(command);
			if (customCommand.isRegex()) {
				receptioner.addCommand(receptioner.getCommandFactory().createCommand(command, customCommand,customCommand.isRegex()));
			} else {
				receptioner.addCommand(receptioner.getCommandFactory().createCommand(command, customCommand));
			}
		}
	}

	public VKCustomCommand getVKCommand(String command) {
		return commands.get(command);
	}
}
