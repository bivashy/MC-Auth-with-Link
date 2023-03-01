package com.bivashy.auth.api.config.link.stage;

import com.bivashy.auth.api.config.duration.ConfigurationDuration;

public interface LinkConfirmationSettings {
    boolean canToggleConfirmation();

    ConfigurationDuration getRemoveDelay();

    int getCodeLength();

    String getCodeCharacters();

    String generateCode();
}
