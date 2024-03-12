package com.bivashy.auth.api.config.importing;

import java.util.Optional;

public interface ImportingSettings {

    Optional<ImportingSourceSettings> sourceSettings(String sourceType);

}
