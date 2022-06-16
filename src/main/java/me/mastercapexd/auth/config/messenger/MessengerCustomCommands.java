package me.mastercapexd.auth.config.messenger;

import me.mastercapexd.auth.config.messenger.command.custom.MessengerCustomConfigurationCommand;
import me.mastercapexd.auth.messenger.commands.custom.CustomCommandExecuteContext;

import java.util.Collection;

public interface MessengerCustomCommands {
    Collection<MessengerCustomConfigurationCommand> execute(CustomCommandExecuteContext context);
}
