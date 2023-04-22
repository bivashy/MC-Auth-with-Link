package me.mastercapexd.auth.config.link;

import java.util.Collection;
import java.util.stream.Collectors;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.link.command.LinkCustomCommandSettings;
import com.bivashy.auth.api.config.link.command.LinkCustomCommands;
import com.bivashy.auth.api.link.command.context.CustomCommandExecutionContext;
import com.bivashy.configuration.ConfigurationHolder;
import com.bivashy.configuration.annotation.ConfigField;
import com.bivashy.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.factory.ConfigurationHolderMapResolverFactory.ConfigurationHolderMap;

public class BaseMessengerCustomCommands implements ConfigurationHolder, LinkCustomCommands {
    @ConfigField("self")
    private ConfigurationHolderMap<BaseCustomCommandSettings> customCommands;

    public BaseMessengerCustomCommands(ConfigurationSectionHolder sectionHolder) {
        AuthPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
    }

    @Override
    public Collection<LinkCustomCommandSettings> execute(CustomCommandExecutionContext context) {
        return customCommands.values().stream().filter(customCommand -> customCommand.shouldExecute(context)).collect(Collectors.toList());
    }
}
