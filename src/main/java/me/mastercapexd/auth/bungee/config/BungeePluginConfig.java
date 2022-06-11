package me.mastercapexd.auth.bungee.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.function.Predicate;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import com.ubivashka.configuration.configurate.holder.ConfigurationHolder;
import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.config.AbstractPluginConfig;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.utils.IOUtils;
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
			YamlConfigurationLoader configurationLoader = YamlConfigurationLoader.builder().indent(4).file(config)
					.build();
			ConfigurationNode configuration = configurationLoader.load();
			File defaultConfigurationFile = File.createTempFile("config", ".yml");
			IOUtils.streamToFile(resourceAsStream, defaultConfigurationFile);
			ConfigurationNode defaultConfiguration = YamlConfigurationLoader.builder().file(defaultConfigurationFile)
					.build().load();
			removeIf(defaultConfiguration,
					(node) -> node.key() instanceof String && ((String) node.key()).equals("not-linked"));
			configuration = configuration.mergeFrom(defaultConfiguration);
			resourceAsStream.close();
			defaultConfigurationFile.delete();
			return configuration;
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		return null;
	}

	private ConfigurationNode removeIf(ConfigurationNode root, Predicate<ConfigurationNode> nodePredicate) {
		root.childrenMap().entrySet().forEach(entry -> {
			if (nodePredicate.test(entry.getValue()))
				root.removeChild(entry.getKey());
			removeIf(entry.getValue(), nodePredicate);
		});
		return root;
	}
}