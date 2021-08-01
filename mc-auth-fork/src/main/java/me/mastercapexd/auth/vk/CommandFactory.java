package me.mastercapexd.auth.vk;

import java.util.ArrayList;
import java.util.List;

import me.mastercapexd.auth.vk.commandhandler.VKCommand;
import me.mastercapexd.auth.vk.commandhandler.VKCommandExecutor;

public class CommandFactory implements VKCommandFactory {

	@Override
	public VKCommand createCommand(String commandName, VKCommandExecutor executor, List<String> aliases,
			boolean isRegex) {
		VKCommand command = new VKCommand(commandName, executor);
		command.setAliases(new ArrayList<>(aliases));
		command.setRegex(isRegex);
		return command;
	}

}
