package me.mastercapexd.auth.config.messenger;

import me.mastercapexd.auth.config.duration.ConfigurationDuration;
import me.mastercapexd.auth.utils.RandomCodeFactory;

public interface MessengerConfirmationSettings {
    boolean canToggleConfirmation();

    ConfigurationDuration getRemoveDelay();

    int getCodeLength();

    String getCodeCharacters();

    default String generateCode() {
        return RandomCodeFactory.generateCode(getCodeLength(), getCodeCharacters());
    }
}
