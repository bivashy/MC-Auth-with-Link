package me.mastercapexd.auth.config.messenger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.ubivashka.configuration.ConfigurationHolder;
import com.ubivashka.configuration.annotation.ConfigField;
import com.ubivashka.configuration.annotation.ImportantField;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.proxy.ProxyPlugin;

public class DefaultCommandPath implements ConfigurationHolder, MessengerCommandPath {
    @ImportantField
    @ConfigField("main-command")
    private String commandPath = null;
    @ConfigField("aliases")
    private List<String> aliases = new ArrayList<>();

    public DefaultCommandPath(ConfigurationSectionHolder sectionHolder) {
        ProxyPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
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
