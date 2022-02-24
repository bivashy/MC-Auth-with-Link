package me.mastercapexd.auth.config;

import java.util.List;
import java.util.regex.Pattern;

import me.mastercapexd.auth.HashType;
import me.mastercapexd.auth.IdentifierType;
import me.mastercapexd.auth.config.messages.bungee.BungeeMessages;
import me.mastercapexd.auth.objects.Server;
import me.mastercapexd.auth.objects.StorageDataSettings;
import me.mastercapexd.auth.storage.StorageType;
import me.mastercapexd.auth.utils.bossbar.BossBarSettings;
import me.mastercapexd.auth.vk.settings.VKSettings;
import net.md_5.bungee.api.config.ServerInfo;

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
	
	List<ServerInfo> getBlockedServers();

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

	long getAuthTime();

	BungeeMessages getBungeeMessages();
	
	BossBarSettings getBossBarSettings();

	ServerInfo findServerInfo(List<Server> servers);
	
	void reload();
	
	
	
	GoogleAuthenticatorSettings getGoogleAuthenticatorSettings();
	


	VKSettings getVKSettings();

	

}