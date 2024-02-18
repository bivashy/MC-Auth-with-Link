package com.bivashy.auth.api.config.importing;

import java.io.File;
import java.util.Optional;

public interface ImportingSourceSettings {

    File getDriverPath();

    String getDriverDownloadUrl();

    String getJdbcUrl();

    String getUsername();

    String getPassword();

    Optional<String> getProperty(String key);

}
