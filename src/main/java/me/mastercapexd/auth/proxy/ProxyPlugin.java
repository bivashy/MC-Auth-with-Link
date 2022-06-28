package me.mastercapexd.auth.proxy;

import java.io.File;
import java.util.regex.Pattern;

import com.ubivashka.configuration.ConfigurationProcessor;
import com.ubivashka.configuration.contexts.defaults.SingleObjectResolverContext;
import com.ubivashka.configuration.holders.ConfigurationSectionHolder;
import com.warrenstrange.googleauth.GoogleAuthenticator;

import me.mastercapexd.auth.account.factories.AccountFactory;
import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.config.ConfigurationHolder;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.config.factories.ConfigurationHolderMapResolverFactory;
import me.mastercapexd.auth.config.factories.ConfigurationHolderResolverFactory;
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
                        (context) -> {
                            Object configurationValue = context.as(SingleObjectResolverContext.class).getConfigurationValue();
                            if (configurationValue == null)
                                return null;
                            return new ConfigurationServer(context.as(SingleObjectResolverContext.class).getConfigurationValue());
                        }).registerFieldResolver(Long.class, (context) -> {
                    Object configurationValue = context.as(SingleObjectResolverContext.class).getConfigurationValue();
                    if (configurationValue == null)
                        return null;
                    return TimeUtils.parseTime(String.valueOf(configurationValue));
                }).registerFieldResolver(Pattern.class, (context) -> {
                    Object configurationValue = context.as(SingleObjectResolverContext.class).getConfigurationValue();
                    if (configurationValue == null)
                        return null;
                    return Pattern.compile(context.as(SingleObjectResolverContext.class).getConfigurationValue().toString());
                }).registerFieldResolverFactory(ConfigurationHolder.class, new ConfigurationHolderResolverFactory())
                .registerFieldResolverFactory(ConfigurationHolderMapResolverFactory.ConfigurationHolderMap.class, new ConfigurationHolderMapResolverFactory())
                .registerFieldResolver(
                        ProxyComponent.class,
                        context -> {
                            ProxyPlugin proxyPlugin = AuthPlugin.getInstance();
                            if (context.configuration().isConfigurationSection(context.path())) {
                                ConfigurationSectionHolder sectionHolder = context.configuration().getSection(context.path());
                                String componentType = sectionHolder.getString("type");
                                switch (componentType) {
                                    case "json":
                                        return proxyPlugin.getCore().componentJson(sectionHolder.getString("value"));
                                    case "legacy":
                                        return proxyPlugin.getCore().componentLegacy(sectionHolder.getString("value"));
                                    case "plain":
                                        return proxyPlugin.getCore().componentPlain(sectionHolder.getString("value"));
                                    default:
                                        throw new IllegalArgumentException(
                                                "Illegal component type in " + context.path() + ":" + componentType + ", available: json,legacy,plain");
                                }
                            }
                            return proxyPlugin.getCore()
                                    .componentLegacy(context.configuration().getString(context.path()));
                        });
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
