package me.mastercapexd.auth.config.link;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.link.command.LinkDispatchCommandsSettings;
import com.bivashy.configuration.ConfigurationHolder;
import com.bivashy.configuration.annotation.ConfigField;
import com.bivashy.configuration.holder.ConfigurationSectionHolder;

import java.util.List;

public class BaseDispatchCommandsSettings implements ConfigurationHolder, LinkDispatchCommandsSettings {
    @ConfigField("enabled")
    private boolean enabled = false;
    @ConfigField("commands-on-link")
    private List<String> commandsOnLink;
    @ConfigField("commands-on-unlink")
    private List<String> commandsOnUnlink;

    public BaseDispatchCommandsSettings() {
    }

    public BaseDispatchCommandsSettings(ConfigurationSectionHolder sectionHolder) {
        AuthPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public List<String> getCommandsOnLink() {
        return commandsOnLink;
    }

    @Override
    public List<String> getCommandsOnUnlink() {
        return commandsOnUnlink;
    }
}
