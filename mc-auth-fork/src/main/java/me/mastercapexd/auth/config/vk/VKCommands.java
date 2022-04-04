package me.mastercapexd.auth.config.vk;

import java.util.HashMap;

import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.ConfigurationHolder;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;
import me.mastercapexd.auth.vk.commands.VKCustomCommand;

public class VKCommands implements ConfigurationHolder {
	private final HashMap<String, VKCustomCommand> commands = new HashMap<>();

	public VKCommands(ConfigurationSectionHolder sectionHolder) {
		for (String command : sectionHolder.getKeys())
			addCommand(command, sectionHolder.getSection(command));
	}

	private void addCommand(String command, ConfigurationSectionHolder sectionHolder) {
		VKCustomCommand vkCommand = new VKCustomCommand(command, sectionHolder);
		commands.put(vkCommand.getCommand(), vkCommand);
	}

	public void registerCommands(VKReceptioner receptioner) {
		for (String command : commands.keySet()) {
			VKCustomCommand customCommand = commands.get(command);
			if (customCommand.isRegex()) {
				receptioner.addCommand(
						receptioner.getCommandFactory().createCommand(command, customCommand, customCommand.isRegex()));
			} else {
				receptioner.addCommand(receptioner.getCommandFactory().createCommand(command, customCommand));
			}
		}
	}

	public VKCustomCommand getVKCommand(String command) {
		return commands.get(command);
	}
}
