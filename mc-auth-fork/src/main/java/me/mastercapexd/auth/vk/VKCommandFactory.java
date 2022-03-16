package me.mastercapexd.auth.vk;

import java.util.ArrayList;
import java.util.List;

import me.mastercapexd.auth.vk.commandhandler.VKCommand;
import me.mastercapexd.auth.vk.commandhandler.VKCommandExecutor;
import me.mastercapexd.auth.vk.settings.VKCommandPath;
import me.mastercapexd.auth.vk.settings.VKCommandPaths;

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

	default VKCommand createCommand(VKCommandExecutor executor, VKCommandPath settings) {
		return createCommand(settings.getCommandPath(), executor, settings.getAliases(), false);
	}

	default VKCommand createCommand(VKCommandExecutor executor, VKCommandPaths mainCommands) {
		return createCommand(executor,mainCommands.getPath(executor.getKey()));
	}
			
}
