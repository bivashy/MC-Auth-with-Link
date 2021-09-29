package me.mastercapexd.auth.vk.settings;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.config.Configuration;

public class VKCommandSettings {
	private String mainCommand;
	private List<String> aliases = new ArrayList<>();

	public VKCommandSettings(Configuration section) {
		mainCommand = section.getString("main-command");
		if (section.contains("aliases"))
			aliases = section.getStringList("aliases");
	}

	public String getMainCommand() {
		return mainCommand;
	}

	public List<String> getAliases() {
		return aliases;
	}
}
