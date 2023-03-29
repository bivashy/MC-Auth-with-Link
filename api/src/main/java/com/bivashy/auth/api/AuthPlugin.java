package com.bivashy.auth.api;

import java.io.File;

import com.bivashy.auth.api.account.AccountFactory;
import com.bivashy.auth.api.bucket.AuthenticatingAccountBucket;
import com.bivashy.auth.api.bucket.AuthenticationStepContextFactoryBucket;
import com.bivashy.auth.api.bucket.AuthenticationStepFactoryBucket;
import com.bivashy.auth.api.bucket.AuthenticationTaskBucket;
import com.bivashy.auth.api.bucket.LinkAuthenticationBucket;
import com.bivashy.auth.api.bucket.LinkConfirmationBucket;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.hook.PluginHook;
import com.bivashy.auth.api.link.user.entry.LinkEntryUser;
import com.bivashy.auth.api.management.LoginManagement;
import com.bivashy.auth.api.provider.LinkTypeProvider;
import com.bivashy.auth.api.server.ServerCore;
import com.bivashy.auth.api.util.Castable;
import com.ubivashka.configuration.ConfigurationProcessor;
import com.warrenstrange.googleauth.GoogleAuthenticator;

import io.github.revxrsal.eventbus.EventBus;
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

    AuthenticationStepFactoryBucket getAuthenticationStepFactoryBucket();

    AuthenticationStepContextFactoryBucket getAuthenticationContextFactoryBucket();

    ConfigurationProcessor getConfigurationProcessor();

    LoginManagement getLoginManagement();

    AuthPlugin setLoginManagement(LoginManagement loginManagement);

    LinkTypeProvider getLinkTypeProvider();

    EventBus getEventBus();

    AuthPlugin setEventBus(EventBus eventBus);

    AuthenticationTaskBucket getAuthenticationTaskBucket();

    AuthenticatingAccountBucket getAuthenticatingAccountBucket();

    LinkConfirmationBucket getLinkConfirmationBucket();

    LinkAuthenticationBucket<LinkEntryUser> getLinkEntryBucket();

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
