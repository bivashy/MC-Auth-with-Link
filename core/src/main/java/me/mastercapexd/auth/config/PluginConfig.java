package me.mastercapexd.auth.config;

import java.util.List;
import java.util.regex.Pattern;

import me.mastercapexd.auth.HashType;
import me.mastercapexd.auth.IdentifierType;
import me.mastercapexd.auth.config.bossbar.BossBarSettings;
import me.mastercapexd.auth.config.google.GoogleAuthenticatorSettings;
import me.mastercapexd.auth.config.message.proxy.ProxyMessages;
import me.mastercapexd.auth.config.server.ConfigurationServer;
import me.mastercapexd.auth.config.storage.DatabaseConfiguration;
import me.mastercapexd.auth.config.storage.LegacyStorageDataSettings;
import me.mastercapexd.auth.config.telegram.TelegramSettings;
import me.mastercapexd.auth.config.vk.VKSettings;
import me.mastercapexd.auth.storage.StorageType;

public interface PluginConfig {
    @Deprecated
    LegacyStorageDataSettings getStorageDataSettings();

    DatabaseConfiguration getDatabaseConfiguration();

    IdentifierType getActiveIdentifierType();

    boolean isNameCaseCheckEnabled();

    HashType getActiveHashType();

    StorageType getStorageType();

    Pattern getNamePattern();

    Pattern getPasswordPattern();

    List<ConfigurationServer> getAuthServers();

    List<ConfigurationServer> getGameServers();

    List<ConfigurationServer> getBlockedServers();

    List<Pattern> getAllowedCommands();

    List<String> getAuthenticationSteps();

    String getAuthenticationStepName(int index);

    boolean isPasswordConfirmationEnabled();

    int getPasswordMinLength();

    int getPasswordMaxLength();

    int getPasswordAttempts();

    int getMaxLoginPerIP();

    int getMessagesDelay();

    long getSessionDurability();

    long getJoinDelay();

    long getAuthTime();

    boolean shouldBlockChat();

    ProxyMessages getProxyMessages();

    BossBarSettings getBossBarSettings();

    ConfigurationServer findServerInfo(List<ConfigurationServer> servers);

    void reload();

    GoogleAuthenticatorSettings getGoogleAuthenticatorSettings();

    TelegramSettings getTelegramSettings();

    VKSettings getVKSettings();
}