package com.bivashy.auth.api.config.importing;

public interface ImportingSourceSettings {
    String getJdbcUrl();

    String getUsername();

    String getPassword();
}
