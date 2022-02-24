package me.mastercapexd.auth.vk;

import java.util.ArrayList;
import java.util.List;

import me.mastercapexd.auth.vk.commandhandler.VKCommand;
import me.mastercapexd.auth.vk.commandhandler.VKCommandExecutor;
import me.mastercapexd.auth.vk.settings.VKCommandSettings;
import me.mastercapexd.auth.vk.settings.VKMainCommands;

public interface VKCommandFactory {
	VKCommand createCommand(String commandName, VKCommandExecutor executor, List<String> aliases,
			boolean isRegex);

	default VKCommand createCommand(String commandName, VKCommandExecutor executor, boolean isRegex) {
		return createCommand(commandName, executor, new ArrayList<>(), isRegex);
	}

	default VKCommand createCommand(String commandName, VKCommandExecutor executor, List<String> aliases) {
		return createCommand(commandName, executor, aliases, false);
	}

	default VKCommand createCommand(String commandName, VKCommandExecutor executor) {
		return createCommand(commandName, executor, new ArrayList<>(), false);
	}

	default VKCommand createCommand(VKCommandExecutor executor, VKCommandSettings settings) {
		return createCommand(settings.getMainCommand(), executor, settings.getAliases(), false);
	}

	default VKCommand createCommand(VKCommandExecutor executor, VKMainCommands mainCommands) {
		return createCommand(executor,mainCommands.getSettings(executor.getKey()));
	}
			
}
