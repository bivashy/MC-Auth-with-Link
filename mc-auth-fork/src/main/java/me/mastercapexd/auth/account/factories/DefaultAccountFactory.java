package me.mastercapexd.auth.account.factories;

import java.util.UUID;

import me.mastercapexd.auth.HashType;
import me.mastercapexd.auth.IdentifierType;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.bungee.account.BungeeAccount;
import me.mastercapexd.auth.link.vk.VKLinkUser;

public class DefaultAccountFactory implements AccountFactory {

	@Override
	public Account createAccount(String id, IdentifierType identifierType, UUID uuid, String name, HashType hashType,
			String password, String googleKey, Integer vkId, boolean vkConfirmationEnabled, long lastQuit,
			String lastIp, long lastSessionStart, long sessionTime) {

		BungeeAccount account = new BungeeAccount(((identifierType == IdentifierType.NAME) ? id.toLowerCase() : id),
				identifierType, uuid, name);

		account.setHashType(hashType);
		account.setPasswordHash(password);
		account.setGoogleKey(googleKey);
		account.setLastQuitTime(lastQuit);
		account.setLastIpAddress(lastIp);
		account.setLastSessionStart(lastSessionStart);

		account.addLinkUser(new VKLinkUser(account, vkId, vkConfirmationEnabled));
		return account;
	}
}