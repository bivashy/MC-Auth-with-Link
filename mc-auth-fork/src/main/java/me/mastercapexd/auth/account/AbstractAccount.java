package me.mastercapexd.auth.account;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import me.mastercapexd.auth.HashType;
import me.mastercapexd.auth.IdentifierType;
import me.mastercapexd.auth.KickResult;
import me.mastercapexd.auth.authentication.step.AuthenticationStep;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.creators.AuthenticationStepCreator;
import me.mastercapexd.auth.authentication.step.steps.NullAuthenticationStep.NullAuthenticationStepCreator;
import me.mastercapexd.auth.link.user.LinkUser;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;

public class AbstractAccount implements Account, Comparable<AbstractAccount> {

	private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();

	private final List<LinkUser> linkUsers = new ArrayList<>();

	private final String id;
	private final IdentifierType identifierType;

	private HashType hashType;

	private final UUID uniqueId;
	private final String name;

	private String passwordHash, lastIpAddress;
	private long lastQuitTime, lastSessionStart;

	private boolean vkConfirmationEnabled = true;

	private Integer currentConfigurationAuthenticationStepCreatorIndex = 0;

	private AuthenticationStep currentAuthenticationStep = null;

	public AbstractAccount(String id, IdentifierType identifierType, UUID uniqueId, String name) {
		this.id = id;
		this.identifierType = identifierType;
		this.uniqueId = uniqueId;
		this.name = name;
	}

	@Override
	public boolean nextAuthenticationStep(AuthenticationStepContext stepContext) {
		if (stepContext == null)
			return false;
		if (currentAuthenticationStep != null && !currentAuthenticationStep.shouldPassToNextStep())
			return false;
		if (PLUGIN.getConfig().getAuthenticationSteps().size() <= currentConfigurationAuthenticationStepCreatorIndex) {
			currentConfigurationAuthenticationStepCreatorIndex = 0;
			return false;
		}
		String stepCreatorName = PLUGIN.getConfig()
				.getAuthenticationStepName(currentConfigurationAuthenticationStepCreatorIndex);
		AuthenticationStepCreator authenticationStepCreator = PLUGIN.getAuthenticationStepCreatorDealership()
				.findFirstByPredicate(stepCreator -> stepCreator.getAuthenticationStepName().equals(stepCreatorName))
				.orElse(new NullAuthenticationStepCreator());
		currentAuthenticationStep = authenticationStepCreator.createNewAuthenticationStep(stepContext);
		currentConfigurationAuthenticationStepCreatorIndex += 1;
		if (currentAuthenticationStep.shouldSkip()) {
			String nextStepName = PLUGIN.getConfig()
					.getAuthenticationStepName(currentConfigurationAuthenticationStepCreatorIndex);
			currentAuthenticationStep = null;
			return nextAuthenticationStep(
					PLUGIN.getAuthenticationContextFactoryDealership().createContext(nextStepName, this));
		}
		return true;
	}

	@Override
	public KickResult kick(String reason) {
		ProxyPlayer proxyPlayer = PLUGIN.getCore().getPlayer(getUniqueId());
		if (proxyPlayer == null)
			return KickResult.PLAYER_OFFLINE;
		proxyPlayer.disconnect(reason);
		return KickResult.KICKED;
	}

	@Override
	public void logout(long sessionDurability) {
		if (!isSessionActive(sessionDurability))
			return;
		setLastSessionStart(0);
	}

	@Override
	public boolean isSessionActive(long sessionDurability) {
		ProxyPlayer proxiedPlayer = identifierType.getPlayer(getId());
		if (proxiedPlayer == null)
			return (getLastSessionStart() + sessionDurability >= System.currentTimeMillis());
		return proxiedPlayer.getRemoteAddress().getHostString().equals(getLastIpAddress())
				&& (getLastSessionStart() + sessionDurability >= System.currentTimeMillis());
	}

	@Override
	public HashType getHashType() {
		return hashType;
	}

	@Override
	public void setHashType(HashType hashType) {
		this.hashType = hashType;
	}

	@Override
	public String getPasswordHash() {
		return passwordHash;
	}

	@Override
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	@Override
	public String getLastIpAddress() {
		return lastIpAddress;
	}

	@Override
	public void setLastIpAddress(String lastIpAddress) {
		this.lastIpAddress = lastIpAddress;
	}

	@Override
	public long getLastQuitTime() {
		return lastQuitTime;
	}

	@Override
	public void setLastQuitTime(long lastQuitTime) {
		this.lastQuitTime = lastQuitTime;
	}

	@Override
	public long getLastSessionStart() {
		return lastSessionStart;
	}

	@Override
	public void setLastSessionStart(long lastSessionStart) {
		this.lastSessionStart = lastSessionStart;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public IdentifierType getIdentifierType() {
		return identifierType;
	}

	@Override
	public UUID getUniqueId() {
		return uniqueId;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int compareTo(AbstractAccount o) {
		return name.compareTo(o.getName());
	}

	@Override
	public boolean isVKConfirmationEnabled() {
		return vkConfirmationEnabled;
	}

	@Override
	public void setVkConfirmationEnabled(boolean vkConfirmationEnabled) {
		this.vkConfirmationEnabled = vkConfirmationEnabled;
	}

	@Override
	public List<LinkUser> getLinkUsers() {
		return Collections.unmodifiableList(linkUsers);
	}

	/**
	 * @param linkUser that will be added to link users. But if list contains
	 *                 {@link LinkUser} with the same linkName, this method will
	 *                 ignore adding linkUser
	 */
	@Override
	public void addLinkUser(LinkUser linkUser) {
		if (linkUser == null)
			return;
		if (linkUsers.stream().anyMatch(listLinkUser -> listLinkUser.getLinkType().equals(linkUser.getLinkType())))
			return;
		linkUsers.add(linkUser);
	}

	@Override
	public Optional<LinkUser> findFirstLinkUser(Predicate<LinkUser> filter) {
		return linkUsers.stream().filter(filter).findFirst();
	}

	@Override
	public int getCurrentConfigurationAuthenticationStepCreatorIndex() {
		return currentConfigurationAuthenticationStepCreatorIndex;
	}

	@Override
	public void setCurrentConfigurationAuthenticationStepCreatorIndex(int index) {
		String stepName = PLUGIN.getConfig().getAuthenticationStepName(index);

		AuthenticationStepCreator authenticationStepCreator = PLUGIN.getAuthenticationStepCreatorDealership()
				.findFirstByPredicate(stepCreator -> stepCreator.getAuthenticationStepName().equals(stepName))
				.orElse(new NullAuthenticationStepCreator());

		AuthenticationStepContext stepContext = PLUGIN.getAuthenticationContextFactoryDealership()
				.createContext(stepName, this);
		currentConfigurationAuthenticationStepCreatorIndex = index;
		currentAuthenticationStep = authenticationStepCreator.createNewAuthenticationStep(stepContext);
	}

	@Override
	public AuthenticationStep getCurrentAuthenticationStep() {
		List<String> authenticationSteps = PLUGIN.getConfig().getAuthenticationSteps();
		String nextStepName = PLUGIN.getConfig()
				.getAuthenticationStepName(currentConfigurationAuthenticationStepCreatorIndex);
		if (currentAuthenticationStep == null)
			setCurrentConfigurationAuthenticationStepCreatorIndex(
					currentConfigurationAuthenticationStepCreatorIndex - 1);

		if (currentAuthenticationStep.shouldPassToNextStep()
				&& currentConfigurationAuthenticationStepCreatorIndex != authenticationSteps.size() - 1)
			nextAuthenticationStep(
					PLUGIN.getAuthenticationContextFactoryDealership().createContext(nextStepName, this));

		return currentAuthenticationStep;
	}

}