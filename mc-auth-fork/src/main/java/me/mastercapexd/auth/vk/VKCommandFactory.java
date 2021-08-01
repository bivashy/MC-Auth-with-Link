package me.mastercapexd.auth.vk;

import java.util.ArrayList;
import java.util.List;

import me.mastercapexd.auth.vk.commandhandler.VKCommand;
import me.mastercapexd.auth.vk.commandhandler.VKCommandExecutor;

public interface VKCommandFactory {
	public VKCommand createCommand(String commandName, VKCommandExecutor executor, List<String> aliases,
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
}
