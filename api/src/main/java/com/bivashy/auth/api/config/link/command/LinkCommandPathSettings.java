package com.bivashy.auth.api.config.link.command;

import com.bivashy.auth.api.util.CollectionUtil;

public interface LinkCommandPathSettings {
    String getCommandPath();

    String[] getAliases();

    default String[] getCommandPaths() {
        String[] commandPath = {getCommandPath()};
        String[] aliases = getAliases();
        return CollectionUtil.addAll(commandPath, aliases);
    }
}
