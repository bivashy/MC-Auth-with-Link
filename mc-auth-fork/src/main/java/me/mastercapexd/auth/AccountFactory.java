package me.mastercapexd.auth;

import java.util.UUID;

public interface AccountFactory {

	Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, HashType hashType,
			String password, String googleKey, Integer vkID, boolean vkConfirmationEnabled, long lastQuit,
			String lastIp, long lastSessionStart, long sessionTime);

	default Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, HashType hashType,
			String password, String googleKey, Integer vkId, boolean vkConfirmationEnabled, long sessionTime) {
		return createAccount(id, identifierType, uuid, name, hashType, password, googleKey, vkId, vkConfirmationEnabled,
				0, null, 0, sessionTime);
	}

	default Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, HashType hashType,
			String password, Integer vkId, boolean vkConfirmationEnabled, String googleKey, long sessionTime) {
		return createAccount(id, identifierType, uuid, name, hashType, password, googleKey, vkId, vkConfirmationEnabled,
				0, null, 0, sessionTime);
	}

	default Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, HashType hashType,
			String password, Integer vkId, boolean vkConfirmationEnabled, long sessionTime) {
		return createAccount(id, identifierType, uuid, name, hashType, password, null, vkId, vkConfirmationEnabled, 0,
				null, 0, sessionTime);
	}

	default Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, HashType hashType,
			String password, String googleKey, Integer vkId, long sessionTime) {
		return createAccount(id, identifierType, uuid, name, hashType, password, googleKey, vkId, true, 0, null, 0,
				sessionTime);
	}

	default Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, HashType hashType,
			String password, Integer vkId, String googleKey, long sessionTime) {
		return createAccount(id, identifierType, uuid, name, hashType, password, googleKey, vkId, true, 0, null, 0,
				sessionTime);
	}

	default Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, HashType hashType,
			String password, Integer vkId, long sessionTime) {
		return createAccount(id, identifierType, uuid, name, hashType, password, null, vkId, true, 0, null, 0,
				sessionTime);
	}

	default Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, HashType hashType,
			String password, long sessionTime) {
		return createAccount(id, identifierType, uuid, name, hashType, password, null, -1, true, 0, null, 0,
				sessionTime);
	}

	default Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, HashType hashType,
			long sessionTime) {
		return createAccount(id, identifierType, uuid, name, hashType, null, null, -1, true, 0, null, 0, sessionTime);
	}
}