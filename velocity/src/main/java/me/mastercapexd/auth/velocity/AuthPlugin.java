package me.mastercapexd.auth.velocity;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import com.ubivashka.configuration.ConfigurationProcessor;
import com.ubivashka.configuration.configurate.SpongeConfigurateProcessor;
import com.ubivashka.messenger.telegram.message.TelegramMessage;
import com.ubivashka.messenger.telegram.providers.TelegramApiProvider;
import com.ubivashka.messenger.vk.message.VkMessage;
import com.ubivashka.messenger.vk.provider.VkApiProvider;
import com.ubivashka.vk.velocity.VelocityVkApiPlugin;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.warrenstrange.googleauth.GoogleAuthenticator;

import io.github.revxrsal.eventbus.EventBus;
import io.github.revxrsal.eventbus.EventBusBuilder;
import me.mastercapexd.auth.account.factories.AccountFactory;
import me.mastercapexd.auth.account.factories.AuthAccountFactory;
import me.mastercapexd.auth.authentication.step.steps.EnterAuthServerAuthenticationStep.EnterAuthServerAuthenticationStepCreator;
import me.mastercapexd.auth.authentication.step.steps.EnterServerAuthenticationStep.EnterServerAuthenticationStepCreator;
import me.mastercapexd.auth.authentication.step.steps.LoginAuthenticationStep.LoginAuthenticationStepCreator;
import me.mastercapexd.auth.authentication.step.steps.NullAuthenticationStep.NullAuthenticationStepCreator;
import me.mastercapexd.auth.authentication.step.steps.RegisterAuthenticationStep.RegisterAuthenticationStepCreator;
import me.mastercapexd.auth.authentication.step.steps.link.GoogleCodeAuthenticationStep.GoogleLinkAuthenticationStepCreator;
import me.mastercapexd.auth.authentication.step.steps.link.TelegramLinkAuthenticationStep.TelegramLinkAuthenticationStepCreator;
import me.mastercapexd.auth.authentication.step.steps.link.VKLinkAuthenticationStep.VKLinkAuthenticationStepCreator;
import me.mastercapexd.auth.bucket.AuthenticatingAccountBucket;
import me.mastercapexd.auth.bucket.DefaultAuthenticatingAccountBucket;
import me.mastercapexd.auth.bucket.LinkAuthenticationBucket;
import me.mastercapexd.auth.config.DefaultPluginConfig;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.dealerships.AuthenticationStepContextFactoryDealership;
import me.mastercapexd.auth.dealerships.AuthenticationStepCreatorDealership;
import me.mastercapexd.auth.hooks.DefaultTelegramPluginHook;
import me.mastercapexd.auth.hooks.TelegramPluginHook;
import me.mastercapexd.auth.hooks.VkPluginHook;
import me.mastercapexd.auth.hooks.limbo.LimboHook;
import me.mastercapexd.auth.link.LinkTypeProvider;
import me.mastercapexd.auth.link.user.confirmation.LinkConfirmationUser;
import me.mastercapexd.auth.link.user.entry.LinkEntryUser;
import me.mastercapexd.auth.listener.AuthenticationAttemptListener;
import me.mastercapexd.auth.management.DefaultLoginManagement;
import me.mastercapexd.auth.management.LoginManagement;
import me.mastercapexd.auth.proxy.ProxyCore;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.ProxyPluginProvider;
import me.mastercapexd.auth.proxy.hooks.PluginHook;
import me.mastercapexd.auth.storage.AccountStorage;
import me.mastercapexd.auth.storage.AuthAccountDatabaseProxy;
import me.mastercapexd.auth.storage.DatabaseHelper;
import me.mastercapexd.auth.task.AuthenticationMessageSendTask;
import me.mastercapexd.auth.task.AuthenticationProgressBarTask;
import me.mastercapexd.auth.task.AuthenticationTaskBucket;
import me.mastercapexd.auth.task.AuthenticationTimeoutTask;
import me.mastercapexd.auth.telegram.commands.TelegramCommandRegistry;
import me.mastercapexd.auth.velocity.adventure.VelocityAudienceProvider;
import me.mastercapexd.auth.velocity.commands.VelocityCommandRegistry;
import me.mastercapexd.auth.velocity.hooks.VelocityVkPluginHook;
import me.mastercapexd.auth.velocity.hooks.limbo.LimboAPIHook;
import me.mastercapexd.auth.velocity.listener.AuthenticationListener;
import me.mastercapexd.auth.velocity.listener.VkDispatchListener;
import me.mastercapexd.auth.vk.commands.VKCommandRegistry;

@Plugin(id = "mcauth", name = "McAuth", version = "1.7.0-RC4", authors = "Ubivashka", dependencies = {@Dependency(id = "vk-api", optional = true),
        @Dependency(id = "telegram-bot-api", optional = true), @Dependency(id = "limboapi", optional = true)})
public class AuthPlugin implements ProxyPlugin {
    private static final ConfigurationProcessor CONFIGURATION_PROCESSOR = new SpongeConfigurateProcessor();
    private static final Map<Class<? extends PluginHook>, PluginHook> HOOKS = new HashMap<>();
    private static AuthPlugin instance;
    private final ProxyServer proxyServer;
    private final ProxyCore core;
    private final File dataFolder;
    private final VelocityAudienceProvider audienceProvider;
    private final AuthenticationTaskBucket taskBucket = new AuthenticationTaskBucket();
    private final LinkAuthenticationBucket<LinkConfirmationUser> linkConfirmationBucket = new LinkAuthenticationBucket<>();
    private final LinkAuthenticationBucket<LinkEntryUser> linkEntryBucket = new LinkAuthenticationBucket<>();
    private AuthenticatingAccountBucket accountBucket;
    private EventBus eventBus = EventBusBuilder.asm().executor(Executors.newFixedThreadPool(4)).build();
    private GoogleAuthenticator googleAuthenticator;
    private PluginConfig config;
    private AccountFactory accountFactory;
    private LinkTypeProvider linkTypeProvider;
    private AccountStorage accountStorage;
    private AuthenticationStepCreatorDealership authenticationStepCreatorDealership;
    private AuthenticationStepContextFactoryDealership authenticationContextFactoryDealership;
    private LoginManagement loginManagement;

    @Inject
    public AuthPlugin(ProxyServer proxyServer, @DataDirectory Path dataDirectory) {
        ProxyPluginProvider.setPluginInstance(this);
        instance = this;
        this.proxyServer = proxyServer;
        this.audienceProvider = new VelocityAudienceProvider(proxyServer);
        this.core = new VelocityProxyCore(proxyServer);
        this.dataFolder = dataDirectory.toFile();
    }

    public static AuthPlugin getInstance() {
        return instance;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
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
        this.accountBucket = new DefaultAuthenticatingAccountBucket(this);

        initializeConfigurationProcessor();
        this.config = new DefaultPluginConfig(this);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        this.accountFactory = new AuthAccountFactory(databaseHelper);
        this.linkTypeProvider = LinkTypeProvider.defaultProvider();
        this.accountStorage = new AuthAccountDatabaseProxy(databaseHelper);
        this.authenticationContextFactoryDealership = new AuthenticationStepContextFactoryDealership();
        this.authenticationStepCreatorDealership = new AuthenticationStepCreatorDealership();
        this.loginManagement = new DefaultLoginManagement(this);

        HOOKS.put(LimboHook.class, new LimboAPIHook());

        this.authenticationStepCreatorDealership.add(new NullAuthenticationStepCreator());
        this.authenticationStepCreatorDealership.add(new LoginAuthenticationStepCreator());
        this.authenticationStepCreatorDealership.add(new RegisterAuthenticationStepCreator());
        this.authenticationStepCreatorDealership.add(new VKLinkAuthenticationStepCreator());
        this.authenticationStepCreatorDealership.add(new GoogleLinkAuthenticationStepCreator());
        this.authenticationStepCreatorDealership.add(new TelegramLinkAuthenticationStepCreator());
        this.authenticationStepCreatorDealership.add(new EnterServerAuthenticationStepCreator());
        this.authenticationStepCreatorDealership.add(new EnterAuthServerAuthenticationStepCreator());

        this.taskBucket.addTask(new AuthenticationTimeoutTask(this));
        this.taskBucket.addTask(new AuthenticationProgressBarTask(this));
        this.taskBucket.addTask(new AuthenticationMessageSendTask(this));

        this.eventBus.register(new AuthenticationAttemptListener(this));
    }

    private void initializeListener() {
        proxyServer.getEventManager().register(this, new AuthenticationListener(this));
    }

    private void initializeCommand() {
        new VelocityCommandRegistry();
    }

    private void initializeTelegram() {
        HOOKS.put(TelegramPluginHook.class, new DefaultTelegramPluginHook());

        TelegramMessage.setDefaultApiProvider(TelegramApiProvider.of(getHook(TelegramPluginHook.class).getTelegramBot()));

        new TelegramCommandRegistry();
    }

    private void initializeVk() {
        HOOKS.put(VkPluginHook.class, new VelocityVkPluginHook());

        VkMessage.setDefaultApiProvider(VkApiProvider.of(VelocityVkApiPlugin.getInstance().getVkApiProvider().getActor(),
                VelocityVkApiPlugin.getInstance().getVkApiProvider().getVkApiClient()));

        proxyServer.getEventManager().register(this, new VkDispatchListener());
        new VKCommandRegistry();
    }

    public ProxyServer getProxyServer() {
        return proxyServer;
    }

    @Override
    public VelocityAudienceProvider getAudienceProvider() {
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
    public LinkTypeProvider getLinkTypeProvider() {
        return linkTypeProvider;
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
        return proxyServer.getPluginManager().fromInstance(this).flatMap(container -> container.getDescription().getVersion()).orElse("");
    }

    @Override
    public File getFolder() {
        return dataFolder;
    }

    @Override
    public ProxyCore getCore() {
        return core;
    }

    @Override
    public ConfigurationProcessor getConfigurationProcessor() {
        return CONFIGURATION_PROCESSOR;
    }

    @Override
    public EventBus getEventBus() {
        return eventBus;
    }

    @Override
    public ProxyPlugin setEventBus(EventBus eventBus) {
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
    public LinkAuthenticationBucket<LinkConfirmationUser> getLinkConfirmationBucket() {
        return linkConfirmationBucket;
    }

    @Override
    public LinkAuthenticationBucket<LinkEntryUser> getLinkEntryBucket() {
        return linkEntryBucket;
    }

    @Override
    public <T extends PluginHook> T getHook(Class<T> clazz) {
        PluginHook hook = HOOKS.get(clazz);
        if (hook == null)
            return null;
        return hook.as(clazz);
    }
}
