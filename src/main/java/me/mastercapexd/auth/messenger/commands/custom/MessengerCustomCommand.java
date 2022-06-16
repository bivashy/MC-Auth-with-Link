package me.mastercapexd.auth.messenger.commands.custom;

import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

public interface MessengerCustomCommand {
    boolean shouldExecute(CustomCommandExecuteContext context);

    String getAnswer();

    ConfigurationSectionHolder getSectionHolder();
}