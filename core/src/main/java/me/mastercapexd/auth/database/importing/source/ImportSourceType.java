package me.mastercapexd.auth.database.importing.source;

import java.util.function.Function;

import com.bivashy.auth.api.config.importing.ImportingSettings;
import com.bivashy.auth.api.config.importing.ImportingSourceSettings;

import me.mastercapexd.auth.database.importing.ImportSource;

public enum ImportSourceType {
    LIMBO_AUTH(config -> new LimboAuthImportSource(extract(config, "limboauth"))),
    LOGIN_SECURITY(config -> new LoginSecurityImportSource(extract(config, "loginsecurity"))),
    AUTH_ME(config -> new AuthMeImportSource(extract(config, "authme")));
    private final Function<ImportingSettings, ImportSource> importSourceFunction;

    ImportSourceType(Function<ImportingSettings, ImportSource> importSourceFunction) {
        this.importSourceFunction = importSourceFunction;
    }

    private static ImportingSourceSettings extract(ImportingSettings settings, String key) {
        return settings.sourceSettings(key).orElseThrow(() -> new IllegalArgumentException("Cannot find importing source settings from key '" + key + "'"));
    }

    public ImportSource create(ImportingSettings settings) {
        return importSourceFunction.apply(settings);
    }
}
