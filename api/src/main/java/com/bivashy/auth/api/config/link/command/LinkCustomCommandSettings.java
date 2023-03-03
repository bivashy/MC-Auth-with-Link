package com.bivashy.auth.api.config.link.command;

import com.bivashy.auth.api.link.command.context.CustomCommandExecutionContext;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

public interface LinkCustomCommandSettings {
    boolean shouldExecute(CustomCommandExecutionContext context);

    String getAnswer();

    ConfigurationSectionHolder getSectionHolder();
}
