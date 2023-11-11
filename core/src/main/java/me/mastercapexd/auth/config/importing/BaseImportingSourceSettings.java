package me.mastercapexd.auth.config.importing;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.importing.ImportingSourceSettings;
import com.bivashy.configuration.ConfigurationHolder;
import com.bivashy.configuration.annotation.ConfigField;
import com.bivashy.configuration.holder.ConfigurationSectionHolder;

public class BaseImportingSourceSettings implements ConfigurationHolder, ImportingSourceSettings {

    @ConfigField("jdbc-url")
    private String jdbcUrl;
    @ConfigField("username")
    private String username;
    @ConfigField("password")
    private String password;

    public BaseImportingSourceSettings(ConfigurationSectionHolder sectionHolder) {
        AuthPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
    }

    @Override
    public String getJdbcUrl() {
        return jdbcUrl;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

}
