package me.mastercapexd.auth.account.factories;

import java.util.UUID;

import me.mastercapexd.auth.HashType;
import me.mastercapexd.auth.IdentifierType;
import me.mastercapexd.auth.account.Account;

public interface AccountFactory {

	public static final Boolean DEFAULT_VK_CONFIRMATION_STATE = true;
	public static final String DEFAULT_PASSWORD = null;
	public static final String DEFAULT_GOOGLE_KEY = null;
	public static final String DEFAULT_IP = null;
	public static final Integer DEFAULT_LAST_QUIT = 0;
	public static final Integer DEFAULT_LAST_SESSION_START = 0;
	public static final Integer DEFAULT_VK_ID = -1;
	
	Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, HashType hashType,
			String password, String googleKey, Integer vkID, boolean vkConfirmationEnabled, long lastQuit,
			String lastIp, long lastSessionStart, long sessionTime);

	default Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, HashType hashType,
			String password, String googleKey, Integer vkId, boolean vkConfirmationEnabled, long sessionTime) {
		return createAccount(id, identifierType, uuid, name, hashType, password, googleKey, vkId, vkConfirmationEnabled,
				DEFAULT_LAST_QUIT, DEFAULT_IP, DEFAULT_LAST_SESSION_START, sessionTime);
	}

	default Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, HashType hashType,
			String password, Integer vkId, boolean vkConfirmationEnabled, String googleKey, long sessionTime) {
		return createAccount(id, identifierType, uuid, name, hashType, password, googleKey, vkId, vkConfirmationEnabled,
				DEFAULT_LAST_QUIT, DEFAULT_IP, DEFAULT_LAST_SESSION_START, sessionTime);
	}

	default Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, HashType hashType,
			String password, Integer vkId, boolean vkConfirmationEnabled, long sessionTime) {
		return createAccount(id, identifierType, uuid, name, hashType, password, DEFAULT_GOOGLE_KEY, vkId, vkConfirmationEnabled, DEFAULT_LAST_QUIT,
				DEFAULT_IP, DEFAULT_LAST_SESSION_START, sessionTime);
	}

	default Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, HashType hashType,
			String password, String googleKey, Integer vkId, long sessionTime) {
		return createAccount(id, identifierType, uuid, name, hashType, password, googleKey, vkId, DEFAULT_VK_CONFIRMATION_STATE, DEFAULT_LAST_QUIT, DEFAULT_IP, DEFAULT_LAST_SESSION_START,
				sessionTime);
	}

	default Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, HashType hashType,
			String password, Integer vkId, String googleKey, long sessionTime) {
		return createAccount(id, identifierType, uuid, name, hashType, password, googleKey, vkId, DEFAULT_VK_CONFIRMATION_STATE, DEFAULT_LAST_QUIT, DEFAULT_IP, DEFAULT_LAST_SESSION_START,
				sessionTime);
	}

	default Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, HashType hashType,
			String password, Integer vkId, long sessionTime) {
		return createAccount(id, identifierType, uuid, name, hashType, password, DEFAULT_GOOGLE_KEY, vkId, DEFAULT_VK_CONFIRMATION_STATE, DEFAULT_LAST_QUIT, DEFAULT_IP, DEFAULT_LAST_SESSION_START,
				sessionTime);
	}

	default Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, HashType hashType,
			String password, long sessionTime) {
		return createAccount(id, identifierType, uuid, name, hashType, password, DEFAULT_GOOGLE_KEY, DEFAULT_VK_ID, DEFAULT_VK_CONFIRMATION_STATE, DEFAULT_LAST_QUIT, DEFAULT_IP, DEFAULT_LAST_SESSION_START,
				sessionTime);
	}

	default Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, HashType hashType,
			long sessionTime) {
		return createAccount(id, identifierType, uuid, name, hashType, DEFAULT_PASSWORD, DEFAULT_GOOGLE_KEY, DEFAULT_VK_ID, DEFAULT_VK_CONFIRMATION_STATE, DEFAULT_LAST_QUIT, DEFAULT_IP, DEFAULT_LAST_SESSION_START, sessionTime);
	}
}