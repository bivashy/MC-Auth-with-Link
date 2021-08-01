package me.mastercapexd.auth.bungee;

import java.util.UUID;

import me.mastercapexd.auth.Account;
import me.mastercapexd.auth.AccountFactory;
import me.mastercapexd.auth.HashType;
import me.mastercapexd.auth.IdentifierType;

public class BungeeAccountFactory implements AccountFactory {

	@Override
	public Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, HashType hashType,
			String password, Integer vkId, long lastQuit, String lastIp, long lastSessionStart,
			long sessionTime) {
		BungeeAccount account = new BungeeAccount(((identifierType == IdentifierType.NAME) ? id.toLowerCase() : id),
				identifierType, uuid, name);
		account.setHashType(hashType);
		account.setPasswordHash(password);
		account.setVKId(vkId);
		account.setLastQuitTime(lastQuit);
		account.setLastIpAddress(lastIp);
		account.setLastSessionStart(lastSessionStart);
		return account;
	}
}