package me.mastercapexd.auth.vk.settings;

import java.util.HashMap;

import net.md_5.bungee.config.Configuration;

public class VKMainCommands {

	private HashMap<String, VKCommandSettings> vkCommands = new HashMap<>();

	public VKMainCommands(Configuration section) {
		for (String commandKey : section.getKeys()) {
			Configuration commandSection = section.getSection(commandKey);
			vkCommands.put(commandKey, new VKCommandSettings(commandSection));
		}
	}

	public VKCommandSettings getSettings(String commandKey) {
		return vkCommands.getOrDefault(commandKey, null);
	}
}
