package me.mastercapexd.auth.config.messenger;

import me.mastercapexd.auth.utils.CollectionUtils;

public interface MessengerCommandPath {
    String getCommandPath();

    String[] getAliases();

    default String[] getCommandPaths() {
        String[] commandPath = {getCommandPath()};
        String[] aliases = getAliases();
        return CollectionUtils.addAll(commandPath, aliases);
    }
}
