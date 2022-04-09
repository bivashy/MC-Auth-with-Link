package me.mastercapexd.auth.account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import me.mastercapexd.auth.HashType;
import me.mastercapexd.auth.IdentifierType;
import me.mastercapexd.auth.KickResult;
import me.mastercapexd.auth.authentication.step.AuthenticationStep;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.link.user.LinkUser;

public interface Account {

	String getId();

	IdentifierType getIdentifierType();

	HashType getHashType();

	void setHashType(HashType hashType);

	UUID getUniqueId();

	String getName();

	String getPasswordHash();

	void setPasswordHash(String passwordHash);

	Integer getVKId();

	void setVKId(Integer id);

	boolean isVKConfirmationEnabled();

	void setVkConfirmationEnabled(boolean vkConfirmationEnabled);

	/**
	 * @return List of link users for example VK link user that holds vk id and etc.
	 */
	List<LinkUser> getLinkUsers();

	/**
	 * @param linkUser that will be added to list of link users
	 */
	void addLinkUser(LinkUser linkUser);

	/**
	 * @param filter
	 * @return ILinkUser that fits filter
	 */
	Optional<LinkUser> findFirstLinkUser(Predicate<LinkUser> filter);

	long getLastQuitTime();

	void setLastQuitTime(long time);

	String getLastIpAddress();

	void setLastIpAddress(String hostString);

	String getGoogleKey();

	void setGoogleKey(String googleKey);

	long getLastSessionStart();

	void setLastSessionStart(long currentTimeMillis);

	int getCurrentConfigurationAuthenticationStepCreatorIndex();

	void setCurrentConfigurationAuthenticationStepCreatorIndex(int index);

	AuthenticationStep getCurrentAuthenticationStep();

	boolean nextAuthenticationStep(AuthenticationStepContext stepContext);

	void logout(long sessionDurability);

	boolean isSessionActive(long sessionDurability);

	KickResult kick(String reason);

	default boolean isRegistered() {
		return getPasswordHash() != null;
	}
}