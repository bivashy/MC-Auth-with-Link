package me.mastercapexd.auth.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.ubivashka.configuration.annotations.ConfigField;
import com.ubivashka.configuration.annotations.ImportantField;
import com.ubivashka.configuration.annotations.SingleObject;
import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.HashType;
import me.mastercapexd.auth.IdentifierType;
import me.mastercapexd.auth.config.bossbar.BossBarSettings;
import me.mastercapexd.auth.config.messages.proxy.ProxyMessages;
import me.mastercapexd.auth.config.server.FillType;
import me.mastercapexd.auth.config.server.Server;
import me.mastercapexd.auth.config.storage.StorageDataSettings;
import me.mastercapexd.auth.config.vk.VKSettings;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.storage.StorageType;

public abstract class AbstractPluginConfig implements PluginConfig {
	@ConfigField("id-type")
	private IdentifierType activeIdentifierType = IdentifierType.NAME;
	@ConfigField("check-name-case")
	private Boolean nameCaseCheckEnabled = true;
	@ConfigField("enable-password-confirm")
	private Boolean passwordConfirmationEnabled = false;
	@ConfigField("hash-type")
	private HashType activeHashType = HashType.SHA256;
	@ConfigField("storage-type")
	private StorageType storageType = StorageType.SQLITE;
	@SingleObject
	@ConfigField("name-regex-pattern")
	private Pattern namePattern = Pattern.compile("[a-zA-Z0-9_]*");
	@SingleObject
	@ConfigField("password-regex-pattern")
	private Pattern passwordPattern = Pattern.compile("[a-zA-Z0-9_$#@^-]*");
	@ConfigField("password-min-length")
	private Integer passwordMinLength = 5;
	@ConfigField("password-max-length")
	private Integer passwordMaxLength = 20;
	@ConfigField("password-attempts")
	private Integer passwordAttempts = 3;
	@SingleObject
	@ConfigField("auth-time")
	private Long authTime = 60L;

	@ImportantField
	@SingleObject
	@ConfigField("auth-servers")
	private List<Server> authServers = null;
	@SingleObject
	@ImportantField
	@ConfigField("game-servers")
	private List<Server> gameServers = null;
	@SingleObject
	@ConfigField("blocked-servers")
	private List<Server> blockedServers = new ArrayList<>();
	@ConfigField("allowed-commands")
	private List<String> allowedCommands = new ArrayList<>();
	@ImportantField
	@ConfigField("data")
	private StorageDataSettings storageDataSettings = null;
	@ConfigField("max-login-per-ip")
	private Integer maxLoginPerIP = 0;
	@ConfigField("messages-delay")
	private Integer messagesDelay = 5;
	@ConfigField("vk")
	private VKSettings vkSettings = new VKSettings();
	@ConfigField("google-authenticator")
	private GoogleAuthenticatorSettings googleAuthenticatorSettings = null;
	@ImportantField
	@ConfigField("messages")
	private ProxyMessages proxyMessages = null;
	@ConfigField("boss-bar")
	private BossBarSettings barSettings = null;
	@ConfigField("fill-type")
	private FillType fillType;
	@SingleObject
	@ConfigField("session-durability")
	private Long sessionDurability = 14400L;
	@SingleObject
	@ConfigField("join-delay")
	private Long joinDelay = 1000L;
	@ConfigField("authentication-steps")
	private List<String> authenticationSteps = new ArrayList<>(
			Arrays.asList("REGISTER", "LOGIN", "VK_LINK", "GOOGLE_LINK", "ENTER_SERVER"));

	protected final ProxyPlugin proxyPlugin;
	protected ConfigurationSectionHolder configurationRoot;

	public AbstractPluginConfig(ProxyPlugin proxyPlugin) {
		this.proxyPlugin = proxyPlugin;
		this.configurationRoot = createConfiguration(proxyPlugin);
		ProxyPlugin.instance().getConfigurationProcessor().resolve(configurationRoot, this);
	}

	@Override
	public Server findServerInfo(List<Server> servers) {
		List<Server> filteredServers = fillType.shuffle(servers.stream().filter(server -> {
			me.mastercapexd.auth.proxy.server.Server proxyServer = proxyPlugin.getCore().serverFromName(server.getId());
			if (proxyServer == null)
				return false;
			if (server.getMaxPlayers() != -1 && (proxyServer.getPlayersCount() >= server.getMaxPlayers()))
				return false;
			return true;
		}).collect(Collectors.toList()));

		if (filteredServers.isEmpty())
			return servers.get(0);

		return filteredServers.get(0);
	}

	@Override
	public void reload() {
		this.configurationRoot = createConfiguration(proxyPlugin);
		ProxyPlugin.instance().getConfigurationProcessor().resolve(configurationRoot, this);
	}

	@Override
	public IdentifierType getActiveIdentifierType() {
		return activeIdentifierType;
	}

	@Override
	public boolean isNameCaseCheckEnabled() {
		return nameCaseCheckEnabled;
	}

	@Override
	public boolean isPasswordConfirmationEnabled() {
		return passwordConfirmationEnabled;
	}

	@Override
	public HashType getActiveHashType() {
		return activeHashType;
	}

	@Override
	public StorageType getStorageType() {
		return storageType;
	}

	@Override
	public Pattern getNamePattern() {
		return namePattern;
	}

	@Override
	public Pattern getPasswordPattern() {
		return passwordPattern;
	}

	public int getPasswordMinLength() {
		return passwordMinLength;
	}

	@Override
	public int getPasswordMaxLength() {
		return passwordMaxLength;
	}

	@Override
	public int getPasswordAttempts() {
		return passwordAttempts;
	}

	@Override
	public long getSessionDurability() {
		return sessionDurability;
	}

	@Override
	public long getJoinDelay() {
		return joinDelay;
	}

	@Override
	public long getAuthTime() {
		return authTime;
	}

	@Override
	public List<Server> getAuthServers() {
		return authServers;
	}

	@Override
	public List<Server> getGameServers() {
		return Collections.unmodifiableList(gameServers);
	}

	@Override
	public List<Server> getBlockedServers() {
		return Collections.unmodifiableList(blockedServers);
	}

	@Override
	public StorageDataSettings getStorageDataSettings() {
		return storageDataSettings;
	}

	@Override
	public ProxyMessages getProxyMessages() {
		return (ProxyMessages) proxyMessages;
	}

	@Override
	public VKSettings getVKSettings() {
		return vkSettings;
	}

	@Override
	public int getMaxLoginPerIP() {
		return maxLoginPerIP;
	}

	@Override
	public int getMessagesDelay() {
		return this.messagesDelay;
	}

	@Override
	public BossBarSettings getBossBarSettings() {
		return this.barSettings;
	}

	@Override
	public GoogleAuthenticatorSettings getGoogleAuthenticatorSettings() {
		return googleAuthenticatorSettings;
	}

	@Override
	public List<String> getAllowedCommands() {
		return Collections.unmodifiableList(allowedCommands);
	}

	@Override
	public List<String> getAuthenticationSteps() {
		return Collections.unmodifiableList(authenticationSteps);
	}

	@Override
	public String getAuthenticationStepName(int index) {
		return index >= 0 && index < authenticationSteps.size() ? authenticationSteps.get(index) : "NULL";
	}

	protected abstract ConfigurationSectionHolder createConfiguration(ProxyPlugin plugin);
}