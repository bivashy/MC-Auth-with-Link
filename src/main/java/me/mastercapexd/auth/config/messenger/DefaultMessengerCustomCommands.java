package me.mastercapexd.auth.config.messenger;

import com.ubivashka.configuration.annotations.ConfigField;
import com.ubivashka.configuration.holders.ConfigurationSectionHolder;
import me.mastercapexd.auth.config.ConfigurationHolder;
import me.mastercapexd.auth.config.factories.ConfigurationHolderMapResolverFactory.ConfigurationHolderMap;
import me.mastercapexd.auth.config.messenger.command.custom.MessengerCustomConfigurationCommand;
import me.mastercapexd.auth.messenger.commands.custom.CustomCommandExecuteContext;
import me.mastercapexd.auth.proxy.ProxyPlugin;

import java.util.Collection;
import java.util.stream.Collectors;

public class DefaultMessengerCustomCommands implements ConfigurationHolder, MessengerCustomCommands {
    @ConfigField
    private ConfigurationHolderMap<MessengerCustomConfigurationCommand> customCommands;

    public DefaultMessengerCustomCommands(ConfigurationSectionHolder sectionHolder) {
        ProxyPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
    }

    @Override
    public Collection<MessengerCustomConfigurationCommand> execute(CustomCommandExecuteContext context) {
        return customCommands.values().stream().filter(customCommand -> customCommand.shouldExecute(context)).collect(Collectors.toList());
    }
}
