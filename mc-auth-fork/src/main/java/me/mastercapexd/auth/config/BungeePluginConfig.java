package me.mastercapexd.auth.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ubivashka.configuration.annotations.ConfigField;
import com.ubivashka.configuration.annotations.ImportantField;
import com.ubivashka.configuration.annotations.SingleObject;

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
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class BungeePluginConfig implements PluginConfig {
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

	private final Plugin plugin;
	private Configuration config;

	public BungeePluginConfig(Plugin plugin) {
		this.plugin = plugin;
		config = loadConfiguration(plugin.getDataFolder(), plugin.getResourceAsStream("config.yml"));
		ProxyPlugin.instance().getConfigurationProcessor().resolve(config, this);
	}

	@Override
	public Server findServerInfo(List<Server> servers) {
		List<Server> filteredServers = fillType.shuffle(servers.stream().filter(server -> {
			ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(server.getId());
			if (serverInfo == null)
				return false;
			if (server.getMaxPlayers() != -1 && (serverInfo.getPlayers().size() >= server.getMaxPlayers()))
				return false;
			return true;
		}).collect(Collectors.toList()));

		if (filteredServers.isEmpty())
			return servers.get(0);

		return filteredServers.get(0);
	}

	@Override
	public void reload() {
		config = loadConfiguration(plugin.getDataFolder(), plugin.getResourceAsStream("config.yml"));
		ProxyPlugin.instance().getConfigurationProcessor().resolve(config, this);
	}

	private Configuration loadConfiguration(File folder, InputStream resourceAsStream) {
		try {
			if (!folder.exists())
				folder.mkdir();

			File config = new File(folder + File.separator + "config.yml");
			if (!config.exists())
				Files.copy(resourceAsStream, config.toPath(), new CopyOption[0]);
			Configuration defaults = ConfigurationProvider.getProvider(YamlConfiguration.class).load(resourceAsStream);
			return ConfigurationProvider.getProvider(YamlConfiguration.class).load(config, defaults);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		return null;
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
	public ProxyMessages getBungeeMessages() {
		return (ProxyMessages) proxyMessages;
	}

	@Override
	public VKSettings getVKSettings() {
		return vkSettings;
	}

	public Configuration getConfig() {
		return config;
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

}