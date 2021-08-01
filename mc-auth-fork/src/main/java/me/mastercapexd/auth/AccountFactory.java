package me.mastercapexd.auth;

import java.util.UUID;

public interface AccountFactory {

	Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, HashType hashType,
			String password, Integer vkID, long lastQuit, String lastIp, long lastSessionStart,
			long sessionTime);

	default Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, HashType hashType,
			String password, String email, Integer vkId, long sessionTime) {
		return createAccount(id, identifierType, uuid, name, hashType, password, vkId, 0, null, 0, sessionTime);
	}

	default Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, HashType hashType,
			String password, Integer vkId, long sessionTime) {
		return createAccount(id, identifierType, uuid, name, hashType, password, vkId, 0, null, 0, sessionTime);
	}

	default Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, HashType hashType,
			String password, String email, long sessionTime) {
		return createAccount(id, identifierType, uuid, name, hashType, password, -1, 0, null, 0, sessionTime);
	}

	default Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, HashType hashType,
			String password, long sessionTime) {
		return createAccount(id, identifierType, uuid, name, hashType, password, -1, 0, null, 0, sessionTime);
	}

	default Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, HashType hashType,
			long sessionTime) {
		return createAccount(id, identifierType, uuid, name, hashType, null, -1, 0, null, 0, sessionTime);
	}
}