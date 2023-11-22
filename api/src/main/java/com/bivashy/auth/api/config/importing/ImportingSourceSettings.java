package com.bivashy.auth.api.config.importing;

import java.util.Optional;

public interface ImportingSourceSettings {
    String getJdbcUrl();

    String getUsername();

    String getPassword();

    Optional<String> getProperty(String key);
}
