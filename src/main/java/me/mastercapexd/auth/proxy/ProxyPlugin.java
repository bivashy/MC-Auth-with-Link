package me.mastercapexd.auth.proxy;

import java.io.File;

import com.ubivashka.configuration.ConfigurationProcessor;
import com.warrenstrange.googleauth.GoogleAuthenticator;

import me.mastercapexd.auth.account.factories.AccountFactory;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.config.duration.ConfigurationDuration;
import me.mastercapexd.auth.config.factories.ConfigurationHolderMapResolverFactory;
import me.mastercapexd.auth.config.resolver.ProxyComponentFieldResolver;
import me.mastercapexd.auth.config.server.ConfigurationServer;
import me.mastercapexd.auth.dealerships.AuthenticationStepContextFactoryDealership;
import me.mastercapexd.auth.dealerships.AuthenticationStepCreatorDealership;
import me.mastercapexd.auth.function.Castable;
import me.mastercapexd.auth.management.LoginManagement;
import me.mastercapexd.auth.proxy.hooks.PluginHook;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import me.mastercapexd.auth.storage.AccountStorage;
import me.mastercapexd.auth.utils.TimeUtils;

public interface ProxyPlugin extends Castable<ProxyPlugin> {
    static ProxyPlugin instance() {
        return ProxyPluginProvider.getPluginInstance();
    }

    default void initializeConfigurationProcessor() {
        getConfigurationProcessor().registerFieldResolver(ConfigurationServer.class,
                        (context) -> new ConfigurationServer(context.getString())).registerFieldResolver(ConfigurationDuration.class,
                        (context) -> new ConfigurationDuration(TimeUtils.parseDuration(context.getString("1s"))))
                .registerFieldResolverFactory(ConfigurationHolderMapResolverFactory.ConfigurationHolderMap.class, new ConfigurationHolderMapResolverFactory())
                .registerFieldResolver(
                        ProxyComponent.class,
                        new ProxyComponentFieldResolver());
    }

    ProxyCore getCore();

    PluginConfig getConfig();

    AccountFactory getAccountFactory();

    AccountStorage getAccountStorage();

    GoogleAuthenticator getGoogleAuthenticator();

    AuthenticationStepCreatorDealership getAuthenticationStepCreatorDealership();

    AuthenticationStepContextFactoryDealership getAuthenticationContextFactoryDealership();

    ConfigurationProcessor getConfigurationProcessor();

    LoginManagement getLoginManagement();

    ProxyPlugin setLoginManagement(LoginManagement loginManagement);

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
