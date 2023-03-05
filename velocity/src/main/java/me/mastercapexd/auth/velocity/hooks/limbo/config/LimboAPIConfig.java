package me.mastercapexd.auth.velocity.hooks.limbo.config;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import com.bivashy.auth.api.asset.resource.ResourceReader;
import com.ubivashka.configuration.ConfigurationProcessor;
import com.ubivashka.configuration.annotation.ConfigField;
import com.ubivashka.configuration.configurate.SpongeConfigurateProcessor;

import me.mastercapexd.auth.velocity.VelocityAuthPluginBootstrap;

public class LimboAPIConfig {
    public static final ConfigurationProcessor CONFIGURATION_PROCESSOR = new SpongeConfigurateProcessor();
    public static final String CONFIGURATION_NAME = "limbo.yml";
    private static final VelocityAuthPluginBootstrap PLUGIN = VelocityAuthPluginBootstrap.getInstance();
    private static final File CONFIGURATION_FILE = new File(PLUGIN.getAuthPlugin().getFolder(), CONFIGURATION_NAME);
    private ConfigurationNode node;
    @ConfigField("values")
    private List<LimboConfig> limboConfigs;

    public LimboAPIConfig() {
        try {
            ResourceReader.defaultReader(VelocityAuthPluginBootstrap.getInstance().getClass().getClassLoader(), CONFIGURATION_NAME)
                    .read()
                    .write(CONFIGURATION_FILE);
            node = YamlConfigurationLoader.builder().file(CONFIGURATION_FILE).build().load();
            CONFIGURATION_PROCESSOR.resolve(node, this);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public List<LimboConfig> getLimboConfigs() {
        return Collections.unmodifiableList(limboConfigs);
    }

    public ConfigurationNode getNode() {
        return node;
    }
}
