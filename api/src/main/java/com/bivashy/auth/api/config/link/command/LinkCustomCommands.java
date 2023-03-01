package com.bivashy.auth.api.config.link.command;

import java.util.Collection;

import com.bivashy.auth.api.link.command.context.CustomCommandExecutionContext;

public interface LinkCustomCommands {
    Collection<LinkCustomCommandSettings> execute(CustomCommandExecutionContext context);
}
