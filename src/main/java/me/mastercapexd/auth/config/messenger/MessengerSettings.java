package me.mastercapexd.auth.config.messenger;

import me.mastercapexd.auth.config.message.messenger.MessengerMessages;
import me.mastercapexd.auth.link.user.info.identificator.LinkUserIdentificator;

public interface MessengerSettings {
    boolean isEnabled();

    int getMaxLinkCount();

    boolean isAdministrator(LinkUserIdentificator identificator);

    MessengerConfirmationSettings getConfirmationSettings();

    MessengerRestoreSettings getRestoreSettings();

    MessengerCustomCommands getCustomCommands();

    MessengerEnterSettings getEnterSettings();

    MessengerCommandPaths getCommandPaths();

    MessengerKeyboards getKeyboards();

    MessengerMessages getMessages();
}
