package me.mastercapexd.auth.config;

import java.util.List;
import java.util.regex.Pattern;

import me.mastercapexd.auth.HashType;
import me.mastercapexd.auth.IdentifierType;
import me.mastercapexd.auth.config.bossbar.BossBarSettings;
import me.mastercapexd.auth.config.messages.proxy.ProxyMessages;
import me.mastercapexd.auth.config.server.Server;
import me.mastercapexd.auth.config.storage.StorageDataSettings;
import me.mastercapexd.auth.config.vk.VKSettings;
import me.mastercapexd.auth.storage.StorageType;

public interface PluginConfig {

	StorageDataSettings getStorageDataSettings();

	IdentifierType getActiveIdentifierType();

	boolean isNameCaseCheckEnabled();

	HashType getActiveHashType();

	StorageType getStorageType();

	Pattern getNamePattern();

	Pattern getPasswordPattern();

	List<Server> getAuthServers();

	List<Server> getGameServers();

	List<Server> getBlockedServers();

	List<String> getAllowedCommands();

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

	ProxyMessages getProxyMessages();

	BossBarSettings getBossBarSettings();

	Server findServerInfo(List<Server> servers);

	void reload();

	GoogleAuthenticatorSettings getGoogleAuthenticatorSettings();

	VKSettings getVKSettings();

}