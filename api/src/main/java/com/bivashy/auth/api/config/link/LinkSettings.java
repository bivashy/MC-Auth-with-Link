package com.bivashy.auth.api.config.link;

import java.util.List;

import com.bivashy.auth.api.config.link.command.LinkCommandPaths;
import com.bivashy.auth.api.config.link.command.LinkCustomCommands;
import com.bivashy.auth.api.config.link.stage.LinkConfirmationSettings;
import com.bivashy.auth.api.config.link.stage.LinkEnterSettings;
import com.bivashy.auth.api.config.link.stage.LinkRestoreSettings;
import com.bivashy.auth.api.config.message.Messages;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.type.LinkConfirmationType;

public interface LinkSettings {
    boolean isEnabled();

    int getMaxLinkCount();

    boolean isAdministrator(LinkUserIdentificator identificator);

    LinkConfirmationSettings getConfirmationSettings();

    LinkRestoreSettings getRestoreSettings();

    LinkCustomCommands getCustomCommands();

    LinkEnterSettings getEnterSettings();

    LinkCommandPaths getCommandPaths();

    LinkKeyboards getKeyboards();

    Messages<String> getMessages();

    List<LinkConfirmationType> getLinkConfirmationTypes();

    List<String> getGameLinkCommands();

    boolean shouldDisableConversationCommands();
}
