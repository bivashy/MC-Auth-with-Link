package me.mastercapexd.auth;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.AuthPluginProvider;
import com.bivashy.auth.api.account.AccountFactory;
import com.bivashy.auth.api.bucket.AuthenticatingAccountBucket;
import com.bivashy.auth.api.bucket.AuthenticationStepContextFactoryBucket;
import com.bivashy.auth.api.bucket.AuthenticationStepFactoryBucket;
import com.bivashy.auth.api.bucket.AuthenticationTaskBucket;
import com.bivashy.auth.api.bucket.LinkAuthenticationBucket;
import com.bivashy.auth.api.bucket.LinkConfirmationBucket;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.config.duration.ConfigurationDuration;
import com.bivashy.auth.api.config.server.ConfigurationServer;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.hook.PluginHook;
import com.bivashy.auth.api.link.user.entry.LinkEntryUser;
import com.bivashy.auth.api.management.LoginManagement;
import com.bivashy.auth.api.provider.LinkTypeProvider;
import com.bivashy.auth.api.server.ServerCore;
import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.configuration.ConfigurationProcessor;
import com.bivashy.configuration.configurate.SpongeConfigurateProcessor;
import com.bivashy.messenger.telegram.message.TelegramMessage;
import com.bivashy.messenger.telegram.providers.TelegramApiProvider;
import com.warrenstrange.googleauth.GoogleAuthenticator;

import io.github.revxrsal.eventbus.EventBus;
import io.github.revxrsal.eventbus.EventBusBuilder;
import me.mastercapexd.auth.account.factory.AuthAccountFactory;
import me.mastercapexd.auth.bucket.BaseAuthenticatingAccountBucket;
import me.mastercapexd.auth.bucket.BaseAuthenticationStepContextFactoryBucket;
import me.mastercapexd.auth.bucket.BaseAuthenticationStepFactoryBucket;
import me.mastercapexd.auth.bucket.BaseAuthenticationTaskBucket;
import me.mastercapexd.auth.bucket.BaseLinkAuthenticationBucket;
import me.mastercapexd.auth.bucket.BaseLinkConfirmationBucket;
import me.mastercapexd.auth.config.BasePluginConfig;
import me.mastercapexd.auth.config.factory.ConfigurationHolderMapResolverFactory;
import me.mastercapexd.auth.config.resolver.ProxyComponentFieldResolver;
import me.mastercapexd.auth.config.resolver.RawURLProviderFieldResolverFactory;
import me.mastercapexd.auth.config.resolver.RawURLProviderFieldResolverFactory.RawURLProvider;
import me.mastercapexd.auth.config.server.BaseConfigurationServer;
import me.mastercapexd.auth.database.AuthAccountDatabaseProxy;
import me.mastercapexd.auth.database.DatabaseHelper;
import me.mastercapexd.auth.hooks.BaseTelegramPluginHook;
import me.mastercapexd.auth.hooks.TelegramPluginHook;
import me.mastercapexd.auth.link.BaseLinkTypeProvider;
import me.mastercapexd.auth.listener.AuthenticationAttemptListener;
import me.mastercapexd.auth.management.BaseLoginManagement;
import me.mastercapexd.auth.step.impl.EnterAuthServerAuthenticationStep.EnterAuthServerAuthenticationStepFactory;
import me.mastercapexd.auth.step.impl.EnterServerAuthenticationStep.EnterServerAuthenticationStepFactory;
import me.mastercapexd.auth.step.impl.LoginAuthenticationStep.LoginAuthenticationStepFactory;
import me.mastercapexd.auth.step.impl.NullAuthenticationStep.NullAuthenticationStepFactory;
import me.mastercapexd.auth.step.impl.RegisterAuthenticationStep.RegisterAuthenticationStepFactory;
import me.mastercapexd.auth.step.impl.link.GoogleCodeAuthenticationStep.GoogleLinkAuthenticationStepFactory;
import me.mastercapexd.auth.step.impl.link.TelegramLinkAuthenticationStep.TelegramLinkAuthenticationStepFactory;
import me.mastercapexd.auth.step.impl.link.VKLinkAuthenticationStep.VKLinkAuthenticationStepFactory;
import me.mastercapexd.auth.task.AuthenticationMessageSendTask;
import me.mastercapexd.auth.task.AuthenticationProgressBarTask;
import me.mastercapexd.auth.task.AuthenticationTimeoutTask;
import me.mastercapexd.auth.telegram.command.TelegramCommandRegistry;
import me.mastercapexd.auth.util.TimeUtils;
import net.kyori.adventure.platform.AudienceProvider;

public class BaseAuthPlugin implements AuthPlugin {
    private final ConfigurationProcessor configurationProcessor = new SpongeConfigurateProcessor();
    private final Map<Class<? extends PluginHook>, PluginHook> hooks = new HashMap<>();
    private final AuthenticationTaskBucket taskBucket = new BaseAuthenticationTaskBucket();
    private final LinkConfirmationBucket linkConfirmationBucket = new BaseLinkConfirmationBucket();
    private final LinkAuthenticationBucket<LinkEntryUser> linkEntryBucket = new BaseLinkAuthenticationBucket<>();
    private final AuthenticationStepFactoryBucket authenticationStepFactoryBucket = new BaseAuthenticationStepFactoryBucket();
    private final String version;
    private final File pluginFolder;
    private AuthenticationStepContextFactoryBucket authenticationStepContextFactoryBucket;
    private AudienceProvider audienceProvider;
    private ServerCore core;
    private File dataFolder;
    private AuthenticatingAccountBucket accountBucket;
    private EventBus eventBus = EventBusBuilder.asm().executor(Executors.newFixedThreadPool(4)).build();
    private GoogleAuthenticator googleAuthenticator;
    private PluginConfig config;
    private AccountFactory accountFactory;
    private LinkTypeProvider linkTypeProvider;
    private AccountDatabase accountDatabase;
    private LoginManagement loginManagement;

    public BaseAuthPlugin(AudienceProvider audienceProvider, String version, File pluginFolder, ServerCore core) {
        AuthPluginProvider.setPluginInstance(this);
        this.core = core;
        this.audienceProvider = audienceProvider;
        this.version = version;
        this.pluginFolder = pluginFolder;
        initializeBasic();
        if (config.getTelegramSettings().isEnabled())
            initializeTelegram();
        if (config.getGoogleAuthenticatorSettings().isEnabled())
            googleAuthenticator = new GoogleAuthenticator();
    }

    private void initializeBasic() {
        this.accountBucket = new BaseAuthenticatingAccountBucket(this);

        this.registerConfigurationProcessor();
        this.config = new BasePluginConfig(this);

        this.authenticationStepContextFactoryBucket = new BaseAuthenticationStepContextFactoryBucket(config.getAuthenticationSteps());
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        this.accountFactory = new AuthAccountFactory(databaseHelper);
        this.linkTypeProvider = BaseLinkTypeProvider.allLinks();
        this.accountDatabase = new AuthAccountDatabaseProxy(databaseHelper);
        this.loginManagement = new BaseLoginManagement(this);

        this.authenticationStepFactoryBucket.add(new NullAuthenticationStepFactory());
        this.authenticationStepFactoryBucket.add(new LoginAuthenticationStepFactory());
        this.authenticationStepFactoryBucket.add(new RegisterAuthenticationStepFactory());
        this.authenticationStepFactoryBucket.add(new VKLinkAuthenticationStepFactory());
        this.authenticationStepFactoryBucket.add(new GoogleLinkAuthenticationStepFactory());
        this.authenticationStepFactoryBucket.add(new TelegramLinkAuthenticationStepFactory());
        this.authenticationStepFactoryBucket.add(new EnterServerAuthenticationStepFactory());
        this.authenticationStepFactoryBucket.add(new EnterAuthServerAuthenticationStepFactory());

        this.taskBucket.addTask(new AuthenticationTimeoutTask(this));
        this.taskBucket.addTask(new AuthenticationProgressBarTask(this));
        this.taskBucket.addTask(new AuthenticationMessageSendTask(this));

        this.eventBus.register(new AuthenticationAttemptListener(this));
    }

    private void initializeTelegram() {
        hooks.put(TelegramPluginHook.class, new BaseTelegramPluginHook());

        TelegramMessage.setDefaultApiProvider(TelegramApiProvider.of(getHook(TelegramPluginHook.class).getTelegramBot()));

        new TelegramCommandRegistry();
    }

    private void registerConfigurationProcessor() {
        configurationProcessor.registerFieldResolver(ConfigurationServer.class, (context) -> new BaseConfigurationServer(context.getString()))
                .registerFieldResolver(ConfigurationDuration.class, (context) -> new ConfigurationDuration(TimeUtils.parseDuration(context.getString("1s"))))
                .registerFieldResolverFactory(ConfigurationHolderMapResolverFactory.ConfigurationHolderMap.class, new ConfigurationHolderMapResolverFactory())
                .registerFieldResolver(ServerComponent.class, new ProxyComponentFieldResolver())
                .registerFieldResolverFactory(RawURLProvider.class, new RawURLProviderFieldResolverFactory())
                .registerFieldResolver(File.class, (context) -> {
                    String path = context.getString("");
                    if (path.isEmpty())
                        return null;
                    return new File(path.replace("%plugin_folder%", getFolder().getAbsolutePath()));
                });
    }

    public BaseAuthPlugin eventBus(EventBus eventBus) {
        this.eventBus = eventBus;
        return this;
    }

    @Override
    public ServerCore getCore() {
        return core;
    }

    @Override
    public AudienceProvider getAudienceProvider() {
        return audienceProvider;
    }

    @Override
    public PluginConfig getConfig() {
        return config;
    }

    @Override
    public AccountFactory getAccountFactory() {
        return accountFactory;
    }

    @Override
    public AccountDatabase getAccountDatabase() {
        return accountDatabase;
    }

    @Override
    public GoogleAuthenticator getGoogleAuthenticator() {
        return googleAuthenticator;
    }

    @Override
    public AuthenticationStepFactoryBucket getAuthenticationStepFactoryBucket() {
        return authenticationStepFactoryBucket;
    }

    @Override
    public AuthenticationStepContextFactoryBucket getAuthenticationContextFactoryBucket() {
        return authenticationStepContextFactoryBucket;
    }

    @Override
    public ConfigurationProcessor getConfigurationProcessor() {
        return configurationProcessor;
    }

    @Override
    public LoginManagement getLoginManagement() {
        return loginManagement;
    }

    @Override
    public AuthPlugin setLoginManagement(LoginManagement loginManagement) {
        this.loginManagement = loginManagement;
        return this;
    }

    @Override
    public LinkTypeProvider getLinkTypeProvider() {
        return linkTypeProvider;
    }

    @Override
    public EventBus getEventBus() {
        return eventBus;
    }

    @Override
    public AuthPlugin setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
        return this;
    }

    @Override
    public AuthenticationTaskBucket getAuthenticationTaskBucket() {
        return taskBucket;
    }

    @Override
    public AuthenticatingAccountBucket getAuthenticatingAccountBucket() {
        return accountBucket;
    }

    @Override
    public LinkConfirmationBucket getLinkConfirmationBucket() {
        return linkConfirmationBucket;
    }

    @Override
    public LinkAuthenticationBucket<LinkEntryUser> getLinkEntryBucket() {
        return linkEntryBucket;
    }

    @Override
    public <T extends PluginHook> T getHook(Class<T> clazz) {
        PluginHook hook = hooks.get(clazz);
        if (hook == null)
            return null;
        return hook.as(clazz);
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public File getFolder() {
        return pluginFolder;
    }

    public <T extends PluginHook> void putHook(Class<? extends T> clazz, T instance) {
        hooks.put(clazz, instance);
    }
}
