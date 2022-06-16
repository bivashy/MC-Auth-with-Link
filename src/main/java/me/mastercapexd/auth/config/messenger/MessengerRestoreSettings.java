package me.mastercapexd.auth.config.messenger;

import me.mastercapexd.auth.utils.RandomCodeFactory;

public interface MessengerRestoreSettings {
    int getCodeLength();

    String getCodeCharacters();

    default String generateCode() {
        return RandomCodeFactory.generateCode(getCodeLength(), getCodeCharacters());
    }
}
