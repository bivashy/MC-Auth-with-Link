package me.mastercapexd.auth.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

import me.mastercapexd.auth.FillType;
import me.mastercapexd.auth.HashType;
import me.mastercapexd.auth.IdentifierType;
import me.mastercapexd.auth.Messages;
import me.mastercapexd.auth.PluginConfig;
import me.mastercapexd.auth.objects.Server;
import me.mastercapexd.auth.objects.StorageDataSettings;
import me.mastercapexd.auth.storage.StorageType;
import me.mastercapexd.auth.utils.TimeUtils;
import me.mastercapexd.auth.utils.bossbar.BossBarSettings;
import me.mastercapexd.auth.vk.settings.VKSettings;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class BungeePluginConfig implements PluginConfig {

	private final IdentifierType activeIdentifierType;
	private final boolean nameCaseCheckEnabled, passwordConfirmationEnabled;
	private final HashType activeHashType;
	private final StorageType storageType;
	private final Pattern namePattern, passwordPattern;
	private final int passwordMinLength, passwordMaxLength, passwordAttempts;
	private final long sessionDurability, authTime;

	private final List<Server> authServers, gameServers;

	private final List<String> blockedServers, captchaServers;

	private final List<String> allowedCommands;

	private final StorageDataSettings storageDataSettings;
	private final int maxLoginPerIP, maxVKLink, messagesDelay;
	private final VKSettings vkSettings;
	private final GoogleAuthenticatorSettings googleAuthenticatorSettings;

	private final Messages bungeeMessages, vkMessages;

	private final VKButtonLabels vkButtonLabels;

	private final BossBarSettings barSettings;

	private final FillType fillType;

	private final Configuration config;

	public BungeePluginConfig(Plugin plugin) {
		config = loadConfiguration(plugin.getDataFolder(), plugin.getResourceAsStream("config.yml"));
		this.activeIdentifierType = IdentifierType.valueOf(config.getString("id-type").toUpperCase());
		this.nameCaseCheckEnabled = config.getBoolean("check-name-case");
		this.passwordConfirmationEnabled = config.getBoolean("enable-password-confirm");
		this.activeHashType = HashType.valueOf(config.getString("hash-type").toUpperCase());
		this.storageType = StorageType.valueOf(config.getString("storage-type").toUpperCase());
		this.namePattern = Pattern.compile(config.getString("name-regex-pattern"));
		this.passwordPattern = Pattern.compile(config.getString("password-regex-pattern"));
		this.passwordMinLength = config.getInt("password-min-length");
		this.passwordMaxLength = config.getInt("password-max-length");
		this.passwordAttempts = config.getInt("password-attempts");
		this.sessionDurability = TimeUtils.parseTime(config.getString("session-durability"));
		this.authTime = config.getLong("auth-time");

		this.allowedCommands = config.getStringList("allowed-commands");

		this.authServers = ImmutableList.copyOf(config.getStringList("auth-servers").stream()
				.map(stringFormat -> new Server(stringFormat)).collect(Collectors.toList()));
		this.gameServers = ImmutableList.copyOf(config.getStringList("game-servers").stream()
				.map(stringFormat -> new Server(stringFormat)).collect(Collectors.toList()));

		this.blockedServers = ImmutableList.copyOf(config.getStringList("blocked-servers"));
		this.captchaServers = ImmutableList.copyOf(config.getStringList("captcha-servers"));

		Configuration data = config.getSection("data");
		this.storageDataSettings = new StorageDataSettings(data.getString("host"), data.getString("database"),
				data.getString("username"), data.getString("password"), data.getInt("port"));

		this.maxLoginPerIP = config.getInt("max-login-per-ip");
		this.maxVKLink = config.getInt("max-vk-link");
		this.messagesDelay = config.getInt("messages-delay");

		Configuration vk = config.getSection("vk");
		this.vkSettings = new VKSettings(vk.getBoolean("enabled"), vk);

		Configuration googleAuthenticatorConfiguration = config.getSection("google-authenticator");
		this.googleAuthenticatorSettings = new GoogleAuthenticatorSettings(googleAuthenticatorConfiguration);

		this.bungeeMessages = new BungeeMessages(config.getSection("messages"));
		this.vkMessages = new VKMessages(vk.getSection("vkmessages"));
		this.vkButtonLabels = new VKButtonLabels(vk.getSection("button-labels"));

		this.barSettings = new BossBarSettings(config.getSection("boss-bar"), bungeeMessages);

		this.fillType = FillType.valueOf(config.getString("fill-type"));
	}

	@Override
	public ServerInfo findServerInfo(List<Server> servers) {
		fillType.shuffle(servers);
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
	public long getAuthTime() {
		return authTime;
	}

	@Override
	public List<Server> getAuthServers() {
		return authServers;
	}

	@Override
	public List<Server> getGameServers() {
		return gameServers;
	}

	@Override
	public List<ServerInfo> getBlockedServers() {
		return blockedServers.stream().map(serverId -> ProxyServer.getInstance().getServerInfo(serverId))
				.collect(Collectors.toList());
	}

	@Override
	public List<ServerInfo> getCaptchaServers() {
		return captchaServers.stream().map(serverId -> ProxyServer.getInstance().getServerInfo(serverId))
				.collect(Collectors.toList());
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
	public VKMessages getVKMessages() {
		return (VKMessages) vkMessages;
	}

	@Override
	public VKSettings getVKSettings() {
		return vkSettings;
	}

	@Override
	public VKButtonLabels getVKButtonLabels() {
		return vkButtonLabels;
	}

	public Configuration getConfig() {
		return config;
	}

	@Override
	public int getMaxLoginPerIP() {
		return maxLoginPerIP;
	}

	@Override
	public int getMaxVKLink() {
		return maxVKLink;
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
		return allowedCommands;
	}

}