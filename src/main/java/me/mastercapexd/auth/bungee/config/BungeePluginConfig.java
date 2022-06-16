package me.mastercapexd.auth.bungee.config;

import com.ubivashka.configuration.configurate.holder.ConfigurationHolder;
import com.ubivashka.configuration.holders.ConfigurationSectionHolder;
import me.mastercapexd.auth.asset.resource.Resource;
import me.mastercapexd.auth.asset.resource.folder.FolderResource;
import me.mastercapexd.auth.asset.resource.folder.FolderResourceReader;
import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.config.AbstractPluginConfig;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import net.md_5.bungee.api.plugin.Plugin;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import org.spongepowered.configurate.transformation.TransformAction;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class BungeePluginConfig extends AbstractPluginConfig {

    public BungeePluginConfig(ProxyPlugin proxyPlugin) {
        super(proxyPlugin);
    }

    @Override
    protected ConfigurationSectionHolder createConfiguration(ProxyPlugin proxyPlugin) {
        Plugin plugin = proxyPlugin.as(AuthPlugin.class);
        return new ConfigurationHolder(loadConfiguration(plugin.getDataFolder(), new FolderResourceReader(plugin.getClass().getClassLoader(), "configurations"
        ).read()));
    }

    private ConfigurationNode loadConfiguration(File folder, FolderResource folderResource) {
        try {
            if (!folder.exists())
                folder.mkdir();
            Stream<Path> walkStream = Files.walk(folder.toPath());
            ConfigurationNode configuration =
                    getConfigurationNode(walkStream.filter(path -> path.toString().endsWith(".yml")).map(Path::toFile).toArray(File[]::new));
            walkStream.close();

            boolean hasConfigurationVersion = !configuration.node("configuration-version").virtual();

            for (Resource resource : folderResource.getResources()) {
                try (InputStream ignored = resource.getStream()) {
                    String realConfigurationName = resource.getName().substring(folderResource.getName().length() + 1);
                    File resourceConfiguration = new File(folder + File.separator + realConfigurationName);
                    ConfigurationNode defaultConfiguration;
                    if (!resourceConfiguration.exists() && !hasConfigurationVersion) {
                        resource.write(resourceConfiguration);
                        defaultConfiguration = YamlConfigurationLoader.builder().file(resourceConfiguration).build().load();
                    } else {
                        File tempDefaultConfigurationFile = File.createTempFile(realConfigurationName, ".yml");
                        resource.write(tempDefaultConfigurationFile);
                        defaultConfiguration = YamlConfigurationLoader.builder().file(tempDefaultConfigurationFile).build().load();
                        tempDefaultConfigurationFile.delete();
                    }

                    ConfigurationTransformation.builder().addAction(NodePath.path(ConfigurationTransformation.WILDCARD_OBJECT,
                            ConfigurationTransformation.WILDCARD_OBJECT, "not-linked"), TransformAction.remove()).build().apply(defaultConfiguration);

                    configuration = configuration.empty() ? defaultConfiguration : configuration.mergeFrom(defaultConfiguration);
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
            return configuration;
        } catch(IOException | URISyntaxException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    private ConfigurationNode getConfigurationNode(File... files) throws ConfigurateException {
        ConfigurationNode rootConfigurationNode = BasicConfigurationNode.root();
        for (File file : files) {
            ConfigurationNode configurationNode = YamlConfigurationLoader.builder().file(file).build().load();
            if (rootConfigurationNode.empty()) {
                rootConfigurationNode = configurationNode;
                continue;
            }
            rootConfigurationNode = rootConfigurationNode.mergeFrom(configurationNode);
        }
        return rootConfigurationNode;
    }
}