package me.mastercapexd.auth.bungee;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.ubivashka.configuration.BungeeConfigurationProcessor;
import com.ubivashka.configuration.ConfigurationProcessor;
import com.ubivashka.configuration.contexts.defaults.SingleObjectResolverContext;
import com.ubivashka.configuration.holders.ConfigurationSectionHolder;
import com.ubivashka.messenger.telegram.message.TelegramMessage;
import com.ubivashka.messenger.telegram.providers.TelegramApiProvider;
import com.ubivashka.messenger.vk.message.VkMessage;
import com.ubivashka.messenger.vk.provider.VkApiProvider;
import com.ubivashka.vk.bungee.BungeeVkApiPlugin;
import com.warrenstrange.googleauth.GoogleAuthenticator;

import me.mastercapexd.auth.account.factories.AccountFactory;
import me.mastercapexd.auth.account.factories.DefaultAccountFactory;
import me.mastercapexd.auth.authentication.step.steps.EnterServerAuthenticationStep.EnterServerAuthenticationStepCreator;
import me.mastercapexd.auth.authentication.step.steps.LoginAuthenticationStep.LoginAuthenticationStepCreator;
import me.mastercapexd.auth.authentication.step.steps.NullAuthenticationStep.NullAuthenticationStepCreator;
import me.mastercapexd.auth.authentication.step.steps.RegisterAuthenticationStep.RegisterAuthenticationStepCreator;
import me.mastercapexd.auth.authentication.step.steps.link.TelegramLinkAuthenticationStep.TelegramLinkAuthenticationStepCreator;
import me.mastercapexd.auth.authentication.step.steps.link.VKLinkAuthenticationStep.VKLinkAuthenticationStepCreator;
import me.mastercapexd.auth.bungee.commands.BungeeCommandsRegistry;
import me.mastercapexd.auth.bungee.config.BungeePluginConfig;
import me.mastercapexd.auth.bungee.hooks.BungeeVkPluginHook;
import me.mastercapexd.auth.bungee.listeners.EventListener;
import me.mastercapexd.auth.bungee.listeners.VkDispatchListener;
import me.mastercapexd.auth.config.ConfigurationHolder;
import me.mastercapexd.auth.config.factories.ConfigurationHolderMapResolverFactory;
import me.mastercapexd.auth.config.factories.ConfigurationHolderMapResolverFactory.ConfigurationHolderMap;
import me.mastercapexd.auth.config.factories.ConfigurationHolderResolverFactory;
import me.mastercapexd.auth.config.server.ConfigurationServer;
import me.mastercapexd.auth.dealerships.AuthenticationStepContextFactoryDealership;
import me.mastercapexd.auth.dealerships.AuthenticationStepCreatorDealership;
import me.mastercapexd.auth.hooks.DefaultTelegramPluginHook;
import me.mastercapexd.auth.hooks.TelegramPluginHook;
import me.mastercapexd.auth.hooks.VkPluginHook;
import me.mastercapexd.auth.management.DefaultLoginManagement;
import me.mastercapexd.auth.management.LoginManagement;
import me.mastercapexd.auth.proxy.ProxyCore;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.ProxyPluginProvider;
import me.mastercapexd.auth.proxy.hooks.PluginHook;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import me.mastercapexd.auth.storage.AccountStorage;
import me.mastercapexd.auth.storage.StorageType;
import me.mastercapexd.auth.storage.mysql.MySQLAccountStorage;
import me.mastercapexd.auth.storage.sqlite.SQLiteAccountStorage;
import me.mastercapexd.auth.telegram.commands.TelegramCommandRegistry;
import me.mastercapexd.auth.utils.TimeUtils;
import me.mastercapexd.auth.vk.commands.VKCommandRegistry;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class AuthPlugin extends Plugin implements ProxyPlugin {
    public static final ConfigurationProcessor CONFIGURATION_PROCESSOR = new BungeeConfigurationProcessor().registerFieldResolver(ConfigurationServer.class,
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
    }).registerFieldResolverFactory(ConfigurationHolder.class, new ConfigurationHolderResolverFactory()).registerFieldResolverFactory(ConfigurationHolderMap.class, new ConfigurationHolderMapResolverFactory());

    private static final Map<Class<? extends PluginHook>, PluginHook> HOOKS = new HashMap<>();
    private static AuthPlugin instance;
    private GoogleAuthenticator googleAuthenticator;
    private BungeePluginConfig config;
    private AccountFactory accountFactory;
    private AccountStorage accountStorage;
    private AuthenticationStepCreatorDealership authenticationStepCreatorDealership;
    private AuthenticationStepContextFactoryDealership authenticationContextFactoryDealership;
    private LoginManagement loginManagement;

    public static AuthPlugin getInstance() {
        if (instance == null)
            throw new UnsupportedOperationException("Plugin not enabled!");
        return instance;
    }

    @Override
    public void onEnable() {
        ProxyPluginProvider.setPluginInstance(this);
        instance = this;
        initialize();
        initializeListener();
        initializeCommand();
        if (config.getVKSettings().isEnabled())
            initializeVk();
        if (config.getTelegramSettings().isEnabled())
            initializeTelegram();
        if (config.getGoogleAuthenticatorSettings().isEnabled())
            googleAuthenticator = new GoogleAuthenticator();

    }

    private void initialize() {
        this.config = new BungeePluginConfig(this);
        this.accountFactory = new DefaultAccountFactory();
        this.accountStorage = loadAccountStorage(config.getStorageType());
        this.authEngine = new BungeeAuthEngine(this, config);
        this.authenticationContextFactoryDealership = new AuthenticationStepContextFactoryDealership();
        this.authenticationStepCreatorDealership = new AuthenticationStepCreatorDealership();
        this.loginManagement = new DefaultLoginManagement(this);
        new BungeeAuthEngine(this, config).start();

        this.authenticationStepCreatorDealership.add(new NullAuthenticationStepCreator());
        this.authenticationStepCreatorDealership.add(new LoginAuthenticationStepCreator());
        this.authenticationStepCreatorDealership.add(new RegisterAuthenticationStepCreator());
        this.authenticationStepCreatorDealership.add(new VKLinkAuthenticationStepCreator());
        this.authenticationStepCreatorDealership.add(new TelegramLinkAuthenticationStepCreator());
        this.authenticationStepCreatorDealership.add(new EnterServerAuthenticationStepCreator());
    }

    private void initializeListener() {
        this.getProxy().getPluginManager().registerListener(this, new EventListener(this, config));
    }

    private void initializeCommand() {
        new BungeeCommandsRegistry();
    }

    private void initializeTelegram() {
        HOOKS.put(TelegramPluginHook.class, new DefaultTelegramPluginHook());

        TelegramMessage.setDefaultApiProvider(TelegramApiProvider.of(getHook(TelegramPluginHook.class).getTelegramBot()));

        new TelegramCommandRegistry();
    }

    private void initializeVk() {
        HOOKS.put(VkPluginHook.class, new BungeeVkPluginHook());

        VkMessage.setDefaultApiProvider(VkApiProvider.of(BungeeVkApiPlugin.getInstance().getVkApiProvider().getActor(),
                BungeeVkApiPlugin.getInstance().getVkApiProvider().getVkApiClient()));

        getProxy().getPluginManager().registerListener(this, new VkDispatchListener());
        new VKCommandRegistry();
    }

    private AccountStorage loadAccountStorage(StorageType storageType) {
        switch (storageType) {
            case SQLITE:
                return new SQLiteAccountStorage(config, accountFactory, this.getDataFolder());
            case MYSQL:
                return new MySQLAccountStorage(config, accountFactory);
        }
        throw new NullPointerException("Wrong account storage!");
    }

    @Override
    public BungeePluginConfig getConfig() {
        return config;
    }

    @Override
    public AccountFactory getAccountFactory() {
        return accountFactory;
    }

    @Override
    public AccountStorage getAccountStorage() {
        return accountStorage;
    }

    @Override
    public GoogleAuthenticator getGoogleAuthenticator() {
        return googleAuthenticator;
    }

    @Override
    public AuthenticationStepCreatorDealership getAuthenticationStepCreatorDealership() {
        return authenticationStepCreatorDealership;
    }

    @Override
    public AuthenticationStepContextFactoryDealership getAuthenticationContextFactoryDealership() {
        return authenticationContextFactoryDealership;
    }

    @Override
    public LoginManagement getLoginManagement() {
        return loginManagement;
    }

    @Override
    public ProxyPlugin setLoginManagement(LoginManagement loginManagement) {
        this.loginManagement = loginManagement;
        return this;
    }

    @Override
    public String getVersion() {
        return getDescription().getVersion();
    }

    @Override
    public File getFolder() {
        return getDataFolder();
    }

    @Override
    public ProxyCore getCore() {
        return BungeeProxyCore.INSTANCE;
    }

    @Override
    public ConfigurationProcessor getConfigurationProcessor() {
        return CONFIGURATION_PROCESSOR;
    }

    @Override
    public <T extends PluginHook> T getHook(Class<T> clazz) {
        PluginHook hook = HOOKS.get(clazz);
        if (hook == null)
            return null;
        return hook.as(clazz);
    }

}