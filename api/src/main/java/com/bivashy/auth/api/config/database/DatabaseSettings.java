package com.bivashy.auth.api.config.database;

import java.io.File;

public interface DatabaseSettings {
    String getConnectionUrl();

    String getUsername();

    String getPassword();

    String getDriverDownloadUrl();

    File getCacheDriverPath();
}
