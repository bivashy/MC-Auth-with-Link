package me.mastercapexd.auth.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.config.bossbar.BossBarSettings;
import com.bivashy.auth.api.config.database.LegacyStorageDataSettings;
import com.bivashy.auth.api.config.duration.ConfigurationDuration;
import com.bivashy.auth.api.config.link.DiscordSettings;
import com.bivashy.auth.api.config.link.GoogleAuthenticatorSettings;
import com.bivashy.auth.api.config.link.TelegramSettings;
import com.bivashy.auth.api.config.link.VKSettings;
import com.bivashy.auth.api.config.message.server.ServerMessages;
import com.bivashy.auth.api.config.server.ConfigurationServer;
import com.bivashy.auth.api.crypto.CryptoProvider;
import com.bivashy.auth.api.database.DatabaseConnectionProvider;
import com.bivashy.auth.api.server.proxy.ProxyServer;
import com.bivashy.auth.api.type.FillType;
import com.bivashy.auth.api.type.IdentifierType;
import com.bivashy.configuration.annotation.ConfigField;
import com.bivashy.configuration.annotation.ImportantField;
import com.bivashy.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.bossbar.BaseBossBarSettings;
import me.mastercapexd.auth.config.discord.BaseDiscordSettings;
import me.mastercapexd.auth.config.google.BaseGoogleAuthenticatorSettings;
import me.mastercapexd.auth.config.message.server.BaseServerMessages;
import me.mastercapexd.auth.config.resolver.RawURLProviderFieldResolverFactory.RawURLProvider;
import me.mastercapexd.auth.config.storage.BaseDatabaseConfiguration;
import me.mastercapexd.auth.config.storage.BaseLegacyStorageDataSettings;
import me.mastercapexd.auth.config.telegram.BaseTelegramSettings;
import me.mastercapexd.auth.config.vk.BaseVKSettings;

public abstract class PluginConfigTemplate implements PluginConfig {

    protected final AuthPlugin plugin;
    private final List<Pattern> allowedPatternCommands;
    protected ConfigurationSectionHolder configurationRoot;
    @ConfigField("auto-migrate-config")
    private boolean autoMigrateConfig;
    @ConfigField("id-type")
    private IdentifierType activeIdentifierType = IdentifierType.NAME;
    @ConfigField("check-name-case")
    private boolean nameCaseCheckEnabled = true;
    @ConfigField("enable-password-confirm")
    private boolean passwordConfirmationEnabled = false;
    @ConfigField("enable-password-in-chat")
    private boolean passwordInChatEnabled = false;
    @ConfigField("hash-type")
    private CryptoProvider activeCryptoProvider;
    @ConfigField("storage-type")
    private DatabaseConnectionProvider databaseConnectionProvider = DatabaseConnectionProvider.SQLITE;
    @ConfigField("name-regex-pattern")
    private Pattern namePattern = Pattern.compile("[a-zA-Z0-9_]*");
    @ConfigField("password-min-length")
    private int passwordMinLength = 5;
    @ConfigField("password-max-length")
    private int passwordMaxLength = 20;
    @ConfigField("password-attempts")
    private int passwordAttempts = 3;
    @ConfigField("auth-time")
    private ConfigurationDuration authTime = new ConfigurationDuration(60);
    @ConfigField("enable-license-support")
    private boolean licenseSupportEnabled = true;
    @ConfigField("license-verify-time")
    private ConfigurationDuration licenseVerifyTime = new ConfigurationDuration(30);
    @ImportantField
    @ConfigField("auth-servers")
    private List<ConfigurationServer> authServers = Collections.emptyList();
    @ImportantField
    @ConfigField("game-servers")
    private List<ConfigurationServer> gameServers = Collections.emptyList();
    @ConfigField("blocked-servers")
    private List<ConfigurationServer> blockedServers = Collections.emptyList();
    @ConfigField("allowed-commands")
    private List<String> allowedCommands = Collections.emptyList();
    @ConfigField("data")
    private BaseLegacyStorageDataSettings legacyStorageDataSettings = null;
    @ConfigField("database")
    private BaseDatabaseConfiguration databaseConfiguration;
    @ConfigField("max-login-per-ip")
    private int maxLoginPerIP = 0;
    @ConfigField("messages-delay")
    private int messagesDelay = 5;
    @ConfigField("telegram")
    private BaseTelegramSettings telegramSettings = new BaseTelegramSettings();
    @ConfigField("vk")
    private BaseVKSettings vkSettings = new BaseVKSettings();
    @ConfigField("discord")
    private BaseDiscordSettings discordSettings = new BaseDiscordSettings();
    @ConfigField("google-authenticator")
    private BaseGoogleAuthenticatorSettings googleAuthenticatorSettings = new BaseGoogleAuthenticatorSettings();
    @ImportantField
    @ConfigField("messages")
    private BaseServerMessages serverMessages = null;
    @ConfigField("boss-bar")
    private BaseBossBarSettings barSettings = new BaseBossBarSettings();
    @ConfigField("fill-type")
    private FillType fillType = FillType.GRADUALLY;
    @ConfigField("session-durability")
    private ConfigurationDuration sessionDurability = new ConfigurationDuration(14400L);
    @ConfigField("join-delay")
    private ConfigurationDuration joinDelay = new ConfigurationDuration(0);
    @ConfigField("block-chat")
    private boolean blockChat = true;
    @ConfigField("limbo-port")
    private IntStream limboPortRange = IntStream.range(49152, 65535);
    @ConfigField("authentication-steps")
    private List<String> authenticationSteps = Arrays.asList("REGISTER", "LOGIN", "VK_LINK", "TELEGRAM_LINK", "GOOGLE_LINK", "ENTER_SERVER");
    @ConfigField("premium-authentication-steps")
    private List<String> premiumAuthenticationSteps = Arrays.asList("VK_LINK", "TELEGRAM_LINK", "GOOGLE_LINK", "ENTER_SERVER");
    @ConfigField("day-plurals")
    private List<String> dayPluralStringList = Arrays.asList("день", "дня", "дней");
    @ConfigField("hour-plurals")
    private List<String> hourPluralStringList = Arrays.asList("час", "часа", "часов");
    @ConfigField("minute-plurals")
    private List<String> minutePluralStringList = Arrays.asList("минута", "минуты", "минут");
    @ConfigField("second-plurals")
    private List<String> secondPluralStringList = Arrays.asList("секунда", "секунды", "секунд");

    public PluginConfigTemplate(AuthPlugin plugin) {
        this.plugin = plugin;
        this.configurationRoot = createConfiguration(plugin);
        plugin.getConfigurationProcessor().resolve(configurationRoot, this);
        this.allowedPatternCommands = allowedCommands.stream().map(Pattern::compile).collect(Collectors.toList());

        if (databaseConfiguration == null && legacyStorageDataSettings != null)
            databaseConfiguration = new BaseDatabaseConfiguration(RawURLProvider.of(databaseConnectionProvider.getConnectionUrl(legacyStorageDataSettings)),
                    databaseConnectionProvider.getDriverDownloadUrl(), legacyStorageDataSettings.getUser(), legacyStorageDataSettings.getPassword());
    }

    @Override
    public ConfigurationServer findServerInfo(List<ConfigurationServer> servers) {
        List<ConfigurationServer> filteredServers = fillType.shuffle(servers.stream().filter(server -> {
            ProxyServer proxyServer = server.asProxyServer();
            if (!proxyServer.isExists()) {
                System.err.println("BaseConfigurationServer with name " + server.getId() + " doesn`t exists in your proxy!");
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
        this.configurationRoot = createConfiguration(plugin);
        AuthPlugin.instance().getConfigurationProcessor().resolve(configurationRoot, this);
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
    public boolean isAutoMigrateConfigEnabled() {
        return autoMigrateConfig;
    }

    @Override
    public boolean isPasswordConfirmationEnabled() {
        return passwordConfirmationEnabled;
    }

    @Override
    public boolean isPasswordInChatEnabled() {
        return passwordInChatEnabled;
    }

    @Override
    public CryptoProvider getActiveHashType() {
        return activeCryptoProvider;
    }

    @Override
    public DatabaseConnectionProvider getStorageType() {
        return databaseConnectionProvider;
    }

    @Override
    public Pattern getNamePattern() {
        return namePattern;
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
    public boolean isLicenseSupportEnabled() {
        return licenseSupportEnabled;
    }

    @Override
    public long getLicenseVerifyTimeMillis() {
        return licenseVerifyTime.getMillis();
    }

    @Override
    public boolean shouldBlockChat() {
        return blockChat;
    }

    @Override
    public List<ConfigurationServer> getAuthServers() {
        return Collections.unmodifiableList(authServers);
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
    public BaseDatabaseConfiguration getDatabaseConfiguration() {
        return databaseConfiguration;
    }

    @Override
    public ServerMessages getServerMessages() {
        return serverMessages;
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
    public DiscordSettings getDiscordSettings() {
        return discordSettings;
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
    public List<String> getPremiumAuthenticationSteps() {
        return Collections.unmodifiableList(premiumAuthenticationSteps);
    }

    @Override
    public List<String> getDayPluralsStringList() {
        return Collections.unmodifiableList(dayPluralStringList);
    }

    @Override
    public List<String> getHourPluralsStringList() {
        return Collections.unmodifiableList(hourPluralStringList);
    }

    @Override
    public List<String> getMinutePluralsStringList() {
        return Collections.unmodifiableList(minutePluralStringList);
    }

    @Override
    public List<String> getSecondPluralsStringList() {
        return Collections.unmodifiableList(secondPluralStringList);
    }

    @Override
    public String getAuthenticationStepName(int index) {
        return index >= 0 && index < authenticationSteps.size()
                ? authenticationSteps.get(index) : "NULL";
    }

    @Override
    public String getPremiumAuthenticationStepName(int index) {
        return index >= 0 && index < premiumAuthenticationSteps.size()
                ? premiumAuthenticationSteps.get(index) : "NULL";
    }

    @Override
    public IntStream getLimboPortRange() {
        return limboPortRange;
    }

    protected abstract ConfigurationSectionHolder createConfiguration(AuthPlugin plugin);

}