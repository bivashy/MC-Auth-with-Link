package me.mastercapexd.auth.bungee;

import java.util.regex.Pattern;

import com.ubivashka.configuration.BungeeConfigurationProcessor;
import com.ubivashka.configuration.ConfigurationProcessor;
import com.ubivashka.configuration.contexts.defaults.SingleObjectResolverContext;
import com.warrenstrange.googleauth.GoogleAuthenticator;

import me.mastercapexd.auth.AuthEngine;
import me.mastercapexd.auth.account.factories.AccountFactory;
import me.mastercapexd.auth.account.factories.DefaultAccountFactory;
import me.mastercapexd.auth.authentication.step.steps.EnterServerAuthenticationStep.EnterServerAuthenticationStepCreator;
import me.mastercapexd.auth.authentication.step.steps.LoginAuthenticationStep.LoginAuthenticationStepCreator;
import me.mastercapexd.auth.authentication.step.steps.NullAuthenticationStep.NullAuthenticationStepCreator;
import me.mastercapexd.auth.authentication.step.steps.RegisterAuthenticationStep.RegisterAuthenticationStepCreator;
import me.mastercapexd.auth.authentication.step.steps.link.VKLinkAuthenticationStep.VKLinkAuthenticationStepCreator;
import me.mastercapexd.auth.bungee.commands.BungeeCommandsRegistry;
import me.mastercapexd.auth.bungee.config.BungeePluginConfig;
import me.mastercapexd.auth.config.ConfigurationHolder;
import me.mastercapexd.auth.config.factories.ConfigurationHolderMapResolverFactory;
import me.mastercapexd.auth.config.factories.ConfigurationHolderMapResolverFactory.ConfigurationHolderMap;
import me.mastercapexd.auth.config.factories.ConfigurationHolderResolverFactory;
import me.mastercapexd.auth.config.server.Server;
import me.mastercapexd.auth.dealerships.AuthenticationStepContextFactoryDealership;
import me.mastercapexd.auth.dealerships.AuthenticationStepCreatorDealership;
import me.mastercapexd.auth.proxy.ProxyCore;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.ProxyPluginProvider;
import me.mastercapexd.auth.storage.AccountStorage;
import me.mastercapexd.auth.storage.StorageType;
import me.mastercapexd.auth.storage.mysql.MySQLAccountStorage;
import me.mastercapexd.auth.storage.sqlite.SQLiteAccountStorage;
import me.mastercapexd.auth.utils.GeoUtils;
import me.mastercapexd.auth.utils.TimeUtils;
import me.mastercapexd.auth.vk.buttonshandler.VKButtonHandler;
import me.mastercapexd.auth.vk.commandhandler.VKCommandHandler;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;
import me.mastercapexd.auth.vk.commands.VKCommandRegistry;
import me.mastercapexd.auth.vk.utils.VKUtils;
import net.md_5.bungee.api.plugin.Plugin;

public class AuthPlugin extends Plugin implements ProxyPlugin {
	public static final ConfigurationProcessor CONFIGURATION_PROCESSOR = new BungeeConfigurationProcessor()
			.registerFieldResolver(Server.class, (context) -> {
				Object configurationValue = context.as(SingleObjectResolverContext.class).getConfigurationValue();
				if (configurationValue == null)
					return null;
				return new Server(context.as(SingleObjectResolverContext.class).getConfigurationValue());
			}).registerFieldResolver(Long.class, (context) -> {
				Object configurationValue = context.as(SingleObjectResolverContext.class).getConfigurationValue();
				if (configurationValue == null)
					return null;
				return TimeUtils.parseTime(String.valueOf(configurationValue));
			}).registerFieldResolver(Pattern.class, (context) -> {
				Object configurationValue = context.as(SingleObjectResolverContext.class).getConfigurationValue();
				if (configurationValue == null)
					return null;
				return Pattern
						.compile(context.as(SingleObjectResolverContext.class).getConfigurationValue().toString());
			}).registerFieldResolverFactory(ConfigurationHolder.class, new ConfigurationHolderResolverFactory())
			.registerFieldResolverFactory(ConfigurationHolderMap.class, new ConfigurationHolderMapResolverFactory());

	private GoogleAuthenticator googleAuthenticator;

	private BungeePluginConfig config;
	private AccountFactory accountFactory;
	private AccountStorage accountStorage;
	private AuthenticationStepCreatorDealership authenticationStepCreatorDealership;
	private AuthenticationStepContextFactoryDealership authenticationContextFactoryDealership;

	private VKCommandHandler vkCommandHandler;

	private VKButtonHandler vkButtonHandler;

	private VKReceptioner vkReceptioner;

	private AuthEngine authEngine;
	private EventListener eventListener;

	private GeoUtils geoUtils = new GeoUtils();

	private VKUtils vkUtils;

	private static AuthPlugin instance;

	@Override
	public void onEnable() {
		ProxyPluginProvider.setPluginInstance(this);
		instance = this;
		initialize();
		initializeListener();
		initializeCommand();
		if (config.getVKSettings().isEnabled())
			registerVK();
		if (config.getGoogleAuthenticatorSettings().isEnabled())
			googleAuthenticator = new GoogleAuthenticator();

	}

	public static AuthPlugin getInstance() {
		if (instance == null)
			throw new UnsupportedOperationException("Plugin not enabled!");
		return instance;
	}

	private void initialize() {
		this.config = new BungeePluginConfig(this);
		this.accountFactory = new DefaultAccountFactory();
		this.accountStorage = loadAccountStorage(config.getStorageType());
		this.authEngine = new BungeeAuthEngine(this, config);
		this.authenticationContextFactoryDealership = new AuthenticationStepContextFactoryDealership();
		this.authenticationStepCreatorDealership = new AuthenticationStepCreatorDealership();
		this.authEngine.start();

		this.authenticationStepCreatorDealership.add(new NullAuthenticationStepCreator());
		this.authenticationStepCreatorDealership.add(new LoginAuthenticationStepCreator());
		this.authenticationStepCreatorDealership.add(new RegisterAuthenticationStepCreator());
		this.authenticationStepCreatorDealership.add(new VKLinkAuthenticationStepCreator());
		this.authenticationStepCreatorDealership.add(new EnterServerAuthenticationStepCreator());

	}

	private void initializeListener() {
		this.eventListener = new EventListener(config, accountFactory, accountStorage);
		this.getProxy().getPluginManager().registerListener(this, eventListener);
	}

	private void initializeCommand() {
		new BungeeCommandsRegistry();
	}

	private void registerVK() {
		this.vkUtils = new VKUtils(config);
		this.vkCommandHandler = new VKCommandHandler();
		this.vkButtonHandler = new VKButtonHandler();
		this.vkReceptioner = new VKReceptioner(this, config, accountStorage, vkCommandHandler, vkButtonHandler);
		this.getProxy().getPluginManager().registerListener(this, vkButtonHandler);
		this.getProxy().getPluginManager().registerListener(this, vkCommandHandler);
		this.config.getVKSettings().getCommands().registerCommands(vkReceptioner);

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

	public VKUtils getVKUtils() {
		return vkUtils;
	}

	public VKReceptioner getVKReceptioner() {
		return vkReceptioner;
	}

	public GeoUtils getGeoUtils() {
		return geoUtils;
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

	public VKCommandHandler getVKCommandHandler() {
		return vkCommandHandler;
	}

	public VKButtonHandler getVKButtonHandler() {
		return vkButtonHandler;
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
	public String getVersion() {
		return getDescription().getVersion();
	}
	
	@Override
	public ProxyCore getCore() {
		return BungeeProxyCore.INSTANCE;
	}

	@Override
	public ConfigurationProcessor getConfigurationProcessor() {
		return CONFIGURATION_PROCESSOR;
	}

	
}