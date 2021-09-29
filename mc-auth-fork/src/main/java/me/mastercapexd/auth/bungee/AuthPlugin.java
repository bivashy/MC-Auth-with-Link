package me.mastercapexd.auth.bungee;

import com.warrenstrange.googleauth.GoogleAuthenticator;

import me.mastercapexd.auth.AccountFactory;
import me.mastercapexd.auth.AuthEngine;
import me.mastercapexd.auth.bungee.command.AuthCommand;
import me.mastercapexd.auth.bungee.command.ChangePasswordCommand;
import me.mastercapexd.auth.bungee.command.GoogleCodeCommand;
import me.mastercapexd.auth.bungee.command.GoogleCommand;
import me.mastercapexd.auth.bungee.command.GoogleUnlinkCommand;
import me.mastercapexd.auth.bungee.command.LoginCommand;
import me.mastercapexd.auth.bungee.command.LogoutCommand;
import me.mastercapexd.auth.bungee.command.RegisterCommand;
import me.mastercapexd.auth.bungee.command.VKLinkCommand;
import me.mastercapexd.auth.config.BungeePluginConfig;
import me.mastercapexd.auth.storage.AccountStorage;
import me.mastercapexd.auth.storage.StorageType;
import me.mastercapexd.auth.storage.mysql.MySQLAccountStorage;
import me.mastercapexd.auth.storage.sqlite.SQLiteAccountStorage;
import me.mastercapexd.auth.utils.GeoUtils;
import me.mastercapexd.auth.utils.ListUtils;
import me.mastercapexd.auth.vk.buttonshandler.VKButtonHandler;
import me.mastercapexd.auth.vk.commandhandler.VKCommandHandler;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;
import me.mastercapexd.auth.vk.utils.VKUtils;
import net.md_5.bungee.api.plugin.Plugin;

public class AuthPlugin extends Plugin {

	private GoogleAuthenticator googleAuthenticator;

	private BungeePluginConfig config;
	private AccountFactory accountFactory;
	private AccountStorage accountStorage;

	private VKCommandHandler vkCommandHandler;

	private VKButtonHandler vkButtonHandler;

	private VKReceptioner vkReceptioner;

	private volatile AuthEngine authEngine;
	private EventListener eventListener;

	private ListUtils listUtils = new ListUtils();

	private GeoUtils geoUtils = new GeoUtils();

	private VKUtils vkUtils;

	private static AuthPlugin instance;

	@Override
	public void onEnable() {
		instance = this;
		registerAuth();
		registerListeners();
		registerCommands();
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

	private void registerAuth() {
		this.config = new BungeePluginConfig(this);
		this.accountFactory = new BungeeAccountFactory();
		this.accountStorage = loadAccountStorage(config.getStorageType());
		this.authEngine = new BungeeAuthEngine(this, config);
		authEngine.start();
	}

	private void registerListeners() {
		this.eventListener = new EventListener(config, accountFactory, accountStorage);
		this.getProxy().getPluginManager().registerListener(this, eventListener);
	}

	private void registerCommands() {
		this.getProxy().getPluginManager().registerCommand(this, new RegisterCommand(config, accountStorage));
		this.getProxy().getPluginManager().registerCommand(this, new LoginCommand(this, config, accountStorage));
		this.getProxy().getPluginManager().registerCommand(this, new LogoutCommand(config, accountStorage));
		this.getProxy().getPluginManager().registerCommand(this, new ChangePasswordCommand(config, accountStorage));
		this.getProxy().getPluginManager().registerCommand(this, new AuthCommand(this, config, accountStorage));
		this.getProxy().getPluginManager().registerCommand(this, new GoogleCodeCommand(this, config, accountStorage));
		this.getProxy().getPluginManager().registerCommand(this, new GoogleCommand(this, config, accountStorage));
		this.getProxy().getPluginManager().registerCommand(this, new GoogleUnlinkCommand(config, accountStorage));
	}

	private void registerVK() {
		this.vkUtils = new VKUtils(config);
		this.vkCommandHandler = new VKCommandHandler();
		this.vkButtonHandler = new VKButtonHandler();
		this.vkReceptioner = new VKReceptioner(this, config, accountStorage, vkCommandHandler, vkButtonHandler);
		this.getProxy().getPluginManager().registerListener(this, vkButtonHandler);
		this.getProxy().getPluginManager().registerListener(this, vkCommandHandler);
		this.getProxy().getPluginManager().registerCommand(this, new VKLinkCommand(this, config, accountStorage));
		this.config.getVKSettings().getCommands().registerCommands(vkReceptioner);
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

	public ListUtils getListUtils() {
		return listUtils;
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

	public BungeePluginConfig getConfig() {
		return config;
	}

	public AccountFactory getAccountFactory() {
		return accountFactory;
	}

	public AccountStorage getAccountStorage() {
		return accountStorage;
	}

	public VKCommandHandler getVKCommandHandler() {
		return vkCommandHandler;
	}

	public VKButtonHandler getVKButtonHandler() {
		return vkButtonHandler;
	}

	public GoogleAuthenticator getGoogleAuthenticator() {
		return googleAuthenticator;
	}
}