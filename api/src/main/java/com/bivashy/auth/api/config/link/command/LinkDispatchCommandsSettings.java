package com.bivashy.auth.api.config.link.command;

import java.util.List;

public interface LinkDispatchCommandsSettings {
    boolean isEnabled();

    List<String> getCommandsOnLink();
    List<String> getCommandsOnUnlink();
}
