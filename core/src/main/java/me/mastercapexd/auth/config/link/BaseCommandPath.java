package me.mastercapexd.auth.config.link;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.link.command.LinkCommandPathSettings;
import com.ubivashka.configuration.ConfigurationHolder;
import com.ubivashka.configuration.annotation.ConfigField;
import com.ubivashka.configuration.annotation.ImportantField;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

public class BaseCommandPath implements ConfigurationHolder, LinkCommandPathSettings {
    @ImportantField
    @ConfigField("main-command")
    private String commandPath = null;
    @ConfigField("aliases")
    private List<String> aliases = new ArrayList<>();

    public BaseCommandPath(ConfigurationSectionHolder sectionHolder) {
        AuthPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
    }

    public String getCommandPath() {
        return commandPath;
    }

    public String[] getAliases() {
        return aliases.toArray(new String[0]);
    }

    public String[] getCommandPaths() {
        String[] originalCommandPath = {commandPath};
        return Stream.concat(Arrays.stream(aliases.toArray(new String[0])), Arrays.stream(originalCommandPath)).toArray(String[]::new);
    }
}