package com.bivashy.auth.api.config.database;

public interface LegacyStorageDataSettings {
    String getHost();

    String getDatabase();

    String getUser();

    String getPassword();

    int getPort();
}
