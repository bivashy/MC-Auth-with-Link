package com.bivashy.auth.api;

import java.io.File;

import com.bivashy.auth.api.account.AccountFactory;
import com.bivashy.auth.api.bucket.AuthenticationStepContextFactoryBucket;
import com.bivashy.auth.api.bucket.AuthenticationStepFactoryBucket;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.hook.PluginHook;
import com.bivashy.auth.api.management.LoginManagement;
import com.bivashy.auth.api.provider.LinkTypeProvider;
import com.bivashy.auth.api.server.ServerCore;
import com.bivashy.auth.api.util.Castable;
import com.warrenstrange.googleauth.GoogleAuthenticator;

import net.kyori.adventure.platform.AudienceProvider;

public interface AuthPlugin extends Castable<AuthPlugin> {
    static AuthPlugin instance() {
        return AuthPluginProvider.getPluginInstance();
    }

    ServerCore getCore();

    AudienceProvider getAudienceProvider();

    PluginConfig getConfig();

    AccountFactory getAccountFactory();

    AccountDatabase getAccountDatabase();

    GoogleAuthenticator getGoogleAuthenticator();

    AuthenticationStepFactoryBucket getAuthenticationStepCreatorBucket();

    AuthenticationStepContextFactoryBucket getAuthenticationContextFactoryBucket();

    LoginManagement getLoginManagement();

    AuthPlugin setLoginManagement(LoginManagement loginManagement);

    LinkTypeProvider getLinkTypeProvider();

    <T extends PluginHook> T getHook(Class<T> clazz);

    String getVersion();

    /**
     * Returns folder of plugin in plugins. For example:
     * some/path/plugins/PluginName
     *
     * @return Plugin folder.
     */
    File getFolder();
}
