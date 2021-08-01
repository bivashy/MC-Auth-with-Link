package me.mastercapexd.auth.vk.commandhandler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.ubivashka.vk.bungee.events.VKMessageEvent;

public class VKCommand {
	private String name;
	private List<String> aliases = new ArrayList<>();
	private boolean isRegex = false;
	private VKCommandExecutor executor;

	public VKCommand(String name, VKCommandExecutor ex) {
		this.name = name;
		this.executor = ex;
	}

	public String getName() {
		return this.name;
	}

	public VKCommandExecutor getExecutor() {
		return this.executor;
	}

	public void execute(VKMessageEvent event, String[] args) {
		this.executor.execute(event, args);
	}

	public boolean isExecuted(String command) {
		if (isMatch(command, this.name))
			return true;
		for (String s : this.aliases)
			if (isMatch(command, s))
				return true;

		return false;
	}

	private boolean isMatch(String command, String match) {
		if (!isRegex) {
			return command.equalsIgnoreCase(match);
		} else {
			return Pattern.matches(match, command);
		}
	}

	public VKCommand addAlias(String alias) {
		this.aliases.add(alias.toLowerCase());
		return this;
	}

	public void setAliases(ArrayList<String> aliases) {
		this.aliases = aliases;
	}

	public boolean isRegex() {
		return isRegex;
	}

	public void setRegex(boolean isRegex) {
		this.isRegex = isRegex;
	}

}
