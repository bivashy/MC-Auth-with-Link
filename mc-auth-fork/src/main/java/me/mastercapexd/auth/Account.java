package me.mastercapexd.auth;

import java.util.UUID;

import me.mastercapexd.auth.utils.RandomCodeFactory;

public interface Account {

	String getId();

	IdentifierType getIdentifierType();

	HashType getHashType();

	UUID getUniqueId();

	String getName();

	String getPasswordHash();

	void setPasswordHash(String passwordHash);

	Integer getVKId();

	void setVKId(Integer id);

	boolean isVKConfirmationEnabled();

	void setVkConfirmationEnabled(boolean vkConfirmationEnabled);

	long getLastQuitTime();

	void setLastQuitTime(long time);

	String getLastIpAddress();

	String getGoogleKey();

	void setGoogleKey(String googleKey);

	long getLastSessionStart();

	SessionResult newSession(HashType hashType, String password);

	SessionResult logout(long sessionDurability);

	boolean isSessionActive(long sessionDurability);

	KickResult kick(String reason);

	default boolean isRegistered() {
		return getPasswordHash() != null;
	}

	default RestoreResult restoreAccount(Integer VKuserID, boolean isAdmin, Integer codeLength) {
		RestoreResult result = RestoreResult.ACCOUNT_VK_NOT_EQUALS;
		result.setPasswordHash(getPasswordHash());
		if (!isAdmin)
			if (getVKId().intValue() != VKuserID.intValue()) {
				return result;
			}
		String newPass = RandomCodeFactory.generateCode(codeLength);
		result = RestoreResult.RESTORED;
		result.setPasswordHash(newPass);
		setPasswordHash(getHashType().hash(newPass));
		return result;
	}

}