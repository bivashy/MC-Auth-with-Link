package me.mastercapexd.auth.vk.settings;

import java.util.HashMap;

import com.ubivashka.config.annotations.ConfigField;
import com.ubivashka.config.processors.BungeeConfigurationHolder;

import net.md_5.bungee.config.Configuration;

public class VKCommandPaths extends BungeeConfigurationHolder{

	@ConfigField
	private HashMap<String, VKCommandPath> vkCommands = new HashMap<>();

	public VKCommandPaths(Configuration section) {
		init(section);
	}

	public VKCommandPath getPath(String commandKey) {
		return vkCommands.getOrDefault(commandKey, null);
	}
}
