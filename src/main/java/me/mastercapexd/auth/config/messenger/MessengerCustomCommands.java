package me.mastercapexd.auth.config.messenger;

import java.util.Collection;

import me.mastercapexd.auth.config.messenger.command.custom.MessengerCustomConfigurationCommand;
import me.mastercapexd.auth.messenger.commands.custom.CustomCommandExecuteContext;

public interface MessengerCustomCommands {
    Collection<MessengerCustomConfigurationCommand> execute(CustomCommandExecuteContext context);
}
