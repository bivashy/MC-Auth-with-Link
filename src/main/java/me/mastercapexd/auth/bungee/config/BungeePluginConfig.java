package me.mastercapexd.auth.bungee.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;

import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import com.ubivashka.configuration.configurate.holder.ConfigurationHolder;
import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.asset.resource.folder.FolderResource;
import me.mastercapexd.auth.asset.resource.folder.FolderResourceReader;
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
				new FolderResourceReader(plugin.getClass().getClassLoader(), "configurations").read(),
				CONFIGURATION_NAME));
	}

	private ConfigurationNode loadConfiguration(File folder, FolderResource folderResource, String configurationName) {
		try {
			if (!folder.exists())
				folder.mkdir();

			ConfigurationNode configuration = getConfigurationNode(Files.walk(folder.toPath())
					.filter(path -> path.toString().endsWith(".yml")).map(Path::toFile).toArray(File[]::new));

			folderResource.getResources().forEach(resource -> {
				try (InputStream resourceStream = resource.getStream()) {
					File resourceConfiguration = new File(folder + File.separator
							+ resource.getName().substring(folderResource.getName().length() + 1));
					if (!resourceConfiguration.exists() && configuration.node("configuration-version").virtual())
						resource.write(resourceConfiguration);

					File tempDefaultConfigurationFile = File.createTempFile(resource.getName(), ".yml");
					resource.write(tempDefaultConfigurationFile);
					ConfigurationNode defaultConfiguration = YamlConfigurationLoader.builder()
							.file(tempDefaultConfigurationFile).build().load();

					removeIf(defaultConfiguration,
							(node) -> node.key() instanceof String && ((String) node.key()).equals("not-linked"));

					configuration.mergeFrom(defaultConfiguration);
					tempDefaultConfigurationFile.delete();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			return configuration;
		} catch (IOException | URISyntaxException exception) {
			exception.printStackTrace();
		}
		return null;
	}

	private ConfigurationNode getConfigurationNode(File... files) throws ConfigurateException {
		ConfigurationNode rootConfigurationNode = null;
		for (File file : files) {
			ConfigurationNode configurationNode = YamlConfigurationLoader.builder().file(file).build().load();
			if (rootConfigurationNode == null) {
				rootConfigurationNode = configurationNode;
				continue;
			}
			rootConfigurationNode = rootConfigurationNode.mergeFrom(configurationNode);
		}
		return rootConfigurationNode;
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