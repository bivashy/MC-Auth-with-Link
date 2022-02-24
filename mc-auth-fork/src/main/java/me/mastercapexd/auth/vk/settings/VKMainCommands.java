package me.mastercapexd.auth.vk.settings;

import java.util.HashMap;

import com.ubivashka.config.annotations.ConfigField;
import com.ubivashka.config.processors.BungeeConfigurationHolder;

import net.md_5.bungee.config.Configuration;

public class VKMainCommands extends BungeeConfigurationHolder{

	@ConfigField
	private HashMap<String, VKCommandSettings> vkCommands = new HashMap<>();

	public VKMainCommands(Configuration section) {
		init(section);
	}

	public VKCommandSettings getSettings(String commandKey) {
		return vkCommands.getOrDefault(commandKey, null);
	}
}
