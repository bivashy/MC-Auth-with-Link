package me.mastercapexd.auth.vk.settings;

import java.util.ArrayList;
import java.util.List;

import com.ubivashka.config.annotations.ConfigField;
import com.ubivashka.config.annotations.ImportantField;
import com.ubivashka.config.processors.BungeeConfigurationHolder;

import net.md_5.bungee.config.Configuration;

public class VKCommandSettings extends BungeeConfigurationHolder{
	@ImportantField
	@ConfigField(path = "main-command")
	private String mainCommand = null;
	@ConfigField(path = "aliases")
	private List<String> aliases = new ArrayList<>();

	public VKCommandSettings(Configuration section) {
		init(section);
	}

	public String getMainCommand() {
		return mainCommand;
	}

	public List<String> getAliases() {
		return aliases;
	}
}
