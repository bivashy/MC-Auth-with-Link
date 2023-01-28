package me.mastercapexd.auth.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.ubivashka.configuration.annotation.ConfigField;
import com.ubivashka.configuration.annotation.ImportantField;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.HashType;
import me.mastercapexd.auth.IdentifierType;
import me.mastercapexd.auth.config.bossbar.BossBarSettings;
import me.mastercapexd.auth.config.duration.ConfigurationDuration;
import me.mastercapexd.auth.config.google.GoogleAuthenticatorSettings;
import me.mastercapexd.auth.config.message.proxy.ProxyMessages;
import me.mastercapexd.auth.config.resolver.RawURLProviderFieldResolverFactory.RawURLProvider;
import me.mastercapexd.auth.config.server.ConfigurationServer;
import me.mastercapexd.auth.config.server.FillType;
import me.mastercapexd.auth.config.storage.DatabaseConfiguration;
import me.mastercapexd.auth.config.storage.LegacyStorageDataSettings;
import me.mastercapexd.auth.config.telegram.TelegramSettings;
import me.mastercapexd.auth.config.vk.VKSettings;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.server.Server;
import me.mastercapexd.auth.storage.StorageType;

public abstract class AbstractPluginConfig implements PluginConfig {
    protected final ProxyPlugin proxyPlugin;
    private final List<Pattern> allowedPatternCommands;
    protected ConfigurationSectionHolder configurationRoot;
    @ConfigField("id-type")
    private IdentifierType activeIdentifierType = IdentifierType.NAME;
    @ConfigField("check-name-case")
    private boolean nameCaseCheckEnabled = true;
    @ConfigField("enable-password-confirm")
    private boolean passwordConfirmationEnabled = false;
    @ConfigField("hash-type")
    private HashType activeHashType = HashType.SHA256;
    @ConfigField("storage-type")
    private StorageType storageType = StorageType.SQLITE;
    @ConfigField("name-regex-pattern")
    private Pattern namePattern = Pattern.compile("[a-zA-Z0-9_]*");
    @ConfigField("password-regex-pattern")
    private Pattern passwordPattern = Pattern.compile("[a-zA-Z0-9_$#@^-]*");
    @ConfigField("password-min-length")
    private int passwordMinLength = 5;
    @ConfigField("password-max-length")
    private int passwordMaxLength = 20;
    @ConfigField("password-attempts")
    private int passwordAttempts = 3;
    @ConfigField("auth-time")
    private ConfigurationDuration authTime = new ConfigurationDuration(60);
    @ImportantField
    @ConfigField("auth-servers")
    private List<ConfigurationServer> authServers = null;
    @ImportantField
    @ConfigField("game-servers")
    private List<ConfigurationServer> gameServers = null;
    @ConfigField("blocked-servers")
    private List<ConfigurationServer> blockedServers = new ArrayList<>();
    @ConfigField("allowed-commands")
    private List<String> allowedCommands = new ArrayList<>();
    @ConfigField("data")
    private LegacyStorageDataSettings legacyStorageDataSettings = null;
    @ConfigField("database")
    private DatabaseConfiguration databaseConfiguration;
    @ConfigField("max-login-per-ip")
    private int maxLoginPerIP = 0;
    @ConfigField("messages-delay")
    private int messagesDelay = 5;
    @ConfigField("telegram")
    private TelegramSettings telegramSettings = new TelegramSettings();
    @ConfigField("vk")
    private VKSettings vkSettings = new VKSettings();
    @ConfigField("google-authenticator")
    private GoogleAuthenticatorSettings googleAuthenticatorSettings = new GoogleAuthenticatorSettings();
    @ImportantField
    @ConfigField("messages")
    private ProxyMessages proxyMessages = null;
    @ConfigField("boss-bar")
    private BossBarSettings barSettings = new BossBarSettings();
    @ConfigField("fill-type")
    private FillType fillType = FillType.GRADUALLY;
    @ConfigField("session-durability")
    private ConfigurationDuration sessionDurability = new ConfigurationDuration(14400L);
    @ConfigField("join-delay")
    private ConfigurationDuration joinDelay = new ConfigurationDuration(0);
    @ConfigField("block-chat")
    private boolean blockChat = true;
    @ConfigField("authentication-steps")
    private List<String> authenticationSteps = Arrays.asList("REGISTER", "LOGIN", "VK_LINK", "TELEGRAM_LINK", "GOOGLE_LINK", "ENTER_SERVER");

    public AbstractPluginConfig(ProxyPlugin proxyPlugin) {
        this.proxyPlugin = proxyPlugin;
        this.configurationRoot = createConfiguration(proxyPlugin);
        proxyPlugin.getConfigurationProcessor().resolve(configurationRoot, this);
        this.allowedPatternCommands = allowedCommands.stream().map(Pattern::compile).collect(Collectors.toList());

        if (databaseConfiguration == null && legacyStorageDataSettings != null)
            databaseConfiguration = new DatabaseConfiguration(RawURLProvider.of(storageType.getConnectionUrl(legacyStorageDataSettings)),
                    storageType.getDriverDownloadUrl(), legacyStorageDataSettings.getUser(), legacyStorageDataSettings.getPassword());
    }

    @Override
    public ConfigurationServer findServerInfo(List<ConfigurationServer> servers) {
        List<ConfigurationServer> filteredServers = fillType.shuffle(servers.stream().filter(server -> {
            Server proxyServer = server.asProxyServer();
            if (!proxyServer.isExists()) {
                System.err.println("ConfigurationServer with name " + server.getId() + " doesn`t exists in your proxy!");
                return false;
            }
            return server.getMaxPlayers() == -1 || (proxyServer.getPlayersCount() < server.getMaxPlayers());
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
        return sessionDurability.getMillis();
    }

    @Override
    public long getJoinDelay() {
        return joinDelay.getMillis();
    }

    @Override
    public long getAuthTime() {
        return authTime.getMillis();
    }

    @Override
    public boolean shouldBlockChat() {
        return blockChat;
    }

    @Override
    public List<ConfigurationServer> getAuthServers() {
        return authServers;
    }

    @Override
    public List<ConfigurationServer> getGameServers() {
        return Collections.unmodifiableList(gameServers);
    }

    @Override
    public List<ConfigurationServer> getBlockedServers() {
        return Collections.unmodifiableList(blockedServers);
    }

    @Override
    public LegacyStorageDataSettings getStorageDataSettings() {
        return legacyStorageDataSettings;
    }

    @Override
    public DatabaseConfiguration getDatabaseConfiguration() {
        return databaseConfiguration;
    }

    @Override
    public ProxyMessages getProxyMessages() {
        return proxyMessages;
    }

    @Override
    public TelegramSettings getTelegramSettings() {
        return telegramSettings;
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
    public List<Pattern> getAllowedCommands() {
        return Collections.unmodifiableList(allowedPatternCommands);
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