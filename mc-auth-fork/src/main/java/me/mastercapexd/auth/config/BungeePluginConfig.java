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

import org.yaml.snakeyaml.Yaml;

import com.google.gson.JsonIOException;
import com.ubivashka.config.annotations.ConfigField;
import com.ubivashka.config.annotations.ConverterType;
import com.ubivashka.config.annotations.ImportantField;
import com.ubivashka.config.processors.BungeeConfigurationHolder;

import me.mastercapexd.auth.FillType;
import me.mastercapexd.auth.HashType;
import me.mastercapexd.auth.IdentifierType;
import me.mastercapexd.auth.config.converters.MessageFieldConverter;
import me.mastercapexd.auth.config.converters.RegexPatternConverter;
import me.mastercapexd.auth.config.converters.ServerConverter;
import me.mastercapexd.auth.config.converters.StringTimeConverter;
import me.mastercapexd.auth.config.messages.bungee.BungeeMessages;
import me.mastercapexd.auth.objects.Server;
import me.mastercapexd.auth.objects.StorageDataSettings;
import me.mastercapexd.auth.storage.StorageType;
import me.mastercapexd.auth.utils.bossbar.BossBarSettings;
import me.mastercapexd.auth.vk.settings.VKSettings;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class BungeePluginConfig extends BungeeConfigurationHolder implements PluginConfig {

	static {
		BungeeConfigurationHolder.getContextProcessorsDealership().put("regex", new RegexPatternConverter());
		BungeeConfigurationHolder.getContextProcessorsDealership().put("time-parser", new StringTimeConverter());
		BungeeConfigurationHolder.getContextProcessorsDealership().put("message-field", new MessageFieldConverter());
		BungeeConfigurationHolder.getContextProcessorsDealership().put("server", new ServerConverter());
	}

	@ConfigField(path = "id-type")
	private IdentifierType activeIdentifierType = IdentifierType.NAME;
	@ConfigField(path = "check-name-case")
	private Boolean nameCaseCheckEnabled = true;
	@ConfigField(path = "enable-password-confirm")
	private Boolean passwordConfirmationEnabled = false;
	@ConfigField(path = "hash-type")
	private HashType activeHashType = HashType.SHA256;
	@ConfigField(path = "storage-type")
	private StorageType storageType = StorageType.SQLITE;
	@ConfigField(path = "name-regex-pattern")
	private Pattern namePattern = Pattern.compile("[a-zA-Z0-9_]*");
	@ConfigField(path = "password-regex-pattern")
	private Pattern passwordPattern = Pattern.compile("[a-zA-Z0-9_$#@^-]*");
	@ConfigField(path = "password-min-length")
	private Integer passwordMinLength = 5;
	@ConfigField(path = "password-max-length")
	private Integer passwordMaxLength = 20;
	@ConfigField(path = "password-attempts")
	private Integer passwordAttempts = 3;
	@ConfigField(path = "auth-time")
	private Long authTime = 60L;

	@ImportantField
	@ConfigField(path = "auth-servers")
	private List<Server> authServers = null;
	@ImportantField
	@ConfigField(path = "game-servers")
	private List<Server> gameServers = null;
	@ConfigField(path = "blocked-servers")
	private List<String> blockedServers = new ArrayList<>();
	@ConfigField(path = "allowed-commands")
	private List<String> allowedCommands = new ArrayList<>();
	@ImportantField
	@ConfigField(path = "data")
	private StorageDataSettings storageDataSettings = null;
	@ConfigField(path = "max-login-per-ip")
	private Integer maxLoginPerIP = 0;
	@ConfigField(path = "messages-delay")
	private Integer messagesDelay = 5;
	@ConfigField(path = "vk")
	private VKSettings vkSettings = new VKSettings();
	@ConfigField(path = "google-authenticator")
	private GoogleAuthenticatorSettings googleAuthenticatorSettings = null;
	@ImportantField
	@ConfigField(path = "messages")
	private BungeeMessages bungeeMessages = null;
	@ConfigField(path = "boss-bar")
	private BossBarSettings barSettings = null;
	@ConfigField(path = "fill-type")
	private FillType fillType;
	@ConverterType("time-parser")
	@ConfigField(path = "session-durability")
	private Long sessionDurability = 14400L;
	@ConverterType("time-parser")
	@ConfigField(path = "join-delay")
	private long joinDelay = 1000L;
	@ConfigField(path = "authentication-steps")
	private List<String> authenticationSteps = new ArrayList<>(
			Arrays.asList("REGISTER", "LOGIN", "VK_LINK", "GOOGLE_LINK", "ENTER_SERVER"));

	private final Plugin plugin;
	private Configuration config;

	public BungeePluginConfig(Plugin plugin) {
		this.plugin = plugin;

		config = loadConfiguration(plugin.getDataFolder(), plugin.getResourceAsStream("config.yml"));
		init(config);
	}

	@Override
	public ServerInfo findServerInfo(List<Server> servers) {
		servers = fillType.shuffle(servers);
		ServerInfo optimal = null;
		for (Server server : servers) {
			ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(server.getId());
			if (serverInfo == null)
				continue;
			if (server.getMaxPlayers() != -1 && (serverInfo.getPlayers().size() >= server.getMaxPlayers()))
				continue;
			optimal = serverInfo;
			break;
		}
		return optimal;
	}

	@Override
	public void reload() {
		config = loadConfiguration(plugin.getDataFolder(), plugin.getResourceAsStream("config.yml"));
		init(config);
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
	public List<ServerInfo> getBlockedServers() {
		return Collections.unmodifiableList(blockedServers.stream()
				.map(serverId -> ProxyServer.getInstance().getServerInfo(serverId)).collect(Collectors.toList()));
	}

	@Override
	public StorageDataSettings getStorageDataSettings() {
		return storageDataSettings;
	}

	@Override
	public BungeeMessages getBungeeMessages() {
		return (BungeeMessages) bungeeMessages;
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
		return index >= 0 && index < authenticationSteps.size()
				? authenticationSteps.get(index)
				: "NULL";
	}

}