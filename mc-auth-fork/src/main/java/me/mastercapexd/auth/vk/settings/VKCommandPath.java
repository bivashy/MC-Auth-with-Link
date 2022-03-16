package me.mastercapexd.auth.vk.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import com.ubivashka.config.annotations.ConfigField;
import com.ubivashka.config.annotations.ImportantField;
import com.ubivashka.config.processors.BungeeConfigurationHolder;

import net.md_5.bungee.config.Configuration;

public class VKCommandPath extends BungeeConfigurationHolder {
	@ImportantField
	@ConfigField(path = "main-command")
	private String commandPath = null;
	@ConfigField(path = "aliases")
	private List<String> aliases = new ArrayList<>();

	public VKCommandPath(Configuration section) {
		init(section);
	}

	public String getCommandPath() {
		return commandPath;
	}

	public List<String> getAliases() {
		return Collections.unmodifiableList(aliases);
	}

	public String[] getCommandPaths() {
		String[] originalCommandPath = { commandPath };
		return Stream.concat(Arrays.stream(aliases.toArray(new String[0])), Arrays.stream(originalCommandPath)).toArray(String[]::new);
	}
}
