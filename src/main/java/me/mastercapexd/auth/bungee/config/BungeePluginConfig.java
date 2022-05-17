package me.mastercapexd.auth.bungee.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import com.ubivashka.configuration.configurate.holder.ConfigurationHolder;
import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.config.AbstractPluginConfig;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeePluginConfig extends AbstractPluginConfig {

	private static final String CONFIGURATION_NAME = "config.yml";

	public BungeePluginConfig(ProxyPlugin proxyPlugin) {
		super(proxyPlugin);
	}

	@Override
	protected ConfigurationSectionHolder createConfiguration(ProxyPlugin proxyPlugin) {
		Plugin plugin = proxyPlugin.as(AuthPlugin.class);
		return new ConfigurationHolder(loadConfiguration(plugin.getDataFolder(),
				plugin.getResourceAsStream(CONFIGURATION_NAME), CONFIGURATION_NAME));
	}

	private ConfigurationNode loadConfiguration(File folder, InputStream resourceAsStream, String configurationName) {
		try {
			if (!folder.exists())
				folder.mkdir();

			File config = new File(folder + File.separator + configurationName);
			if (!config.exists())
				Files.copy(resourceAsStream, config.toPath(), new CopyOption[0]);
			return YamlConfigurationLoader.builder().file(config).build().load();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		return null;
	}

}