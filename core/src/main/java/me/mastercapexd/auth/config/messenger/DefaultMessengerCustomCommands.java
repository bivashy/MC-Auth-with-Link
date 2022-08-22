package me.mastercapexd.auth.config.messenger;

import java.util.Collection;
import java.util.stream.Collectors;

import com.ubivashka.configuration.ConfigurationHolder;
import com.ubivashka.configuration.annotation.ConfigField;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.factories.ConfigurationHolderMapResolverFactory.ConfigurationHolderMap;
import me.mastercapexd.auth.config.messenger.command.custom.MessengerCustomConfigurationCommand;
import me.mastercapexd.auth.messenger.commands.custom.CustomCommandExecuteContext;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class DefaultMessengerCustomCommands implements ConfigurationHolder, MessengerCustomCommands {
    @ConfigField("self")
    private ConfigurationHolderMap<MessengerCustomConfigurationCommand> customCommands;

    public DefaultMessengerCustomCommands(ConfigurationSectionHolder sectionHolder) {
        ProxyPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
    }

    @Override
    public Collection<MessengerCustomConfigurationCommand> execute(CustomCommandExecuteContext context) {
        return customCommands.values().stream().filter(customCommand -> customCommand.shouldExecute(context)).collect(Collectors.toList());
    }
}
