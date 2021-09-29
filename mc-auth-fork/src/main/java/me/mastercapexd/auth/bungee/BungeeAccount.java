package me.mastercapexd.auth.bungee;

import java.util.UUID;

import me.mastercapexd.auth.Account;
import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.HashType;
import me.mastercapexd.auth.IdentifierType;
import me.mastercapexd.auth.KickResult;
import me.mastercapexd.auth.SessionResult;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeAccount implements Account, Comparable<BungeeAccount> {

	private final String id;
	private final IdentifierType identifierType;

	private HashType hashType;

	private final UUID uniqueId;
	private final String name;

	private String passwordHash, lastIpAddress, googleKey;
	private Integer vkId;
	private long lastQuitTime, lastSessionStart;

	private boolean vkConfirmationEnabled = true;

	public BungeeAccount(String id, IdentifierType identifierType, UUID uniqueId, String name) {
		this.id = id;
		this.identifierType = identifierType;
		this.uniqueId = uniqueId;
		this.name = name;
	}

	@SuppressWarnings("deprecation")
	@Override
	public SessionResult newSession(HashType hashType, String password) {
		ProxiedPlayer proxiedPlayer = identifierType.getPlayer(getId());
		String passwordHash = getHashType().hash(password);

		if (!isRegistered()) {
			setPasswordHash(passwordHash);
			setLastIpAddress(proxiedPlayer.getAddress().getHostString());
			setLastSessionStart(System.currentTimeMillis());
			return SessionResult.REGISTER_SUCCESS;
		}

		if (getHashType().checkHash(password, getPasswordHash())) {
			if (getHashType() != hashType) {
				setHashType(hashType);
				setPasswordHash(hashType.hash(password));
			}
			setLastIpAddress(proxiedPlayer.getAddress().getHostString());
			setLastSessionStart(System.currentTimeMillis());
			if (getVKId() != null && getVKId() != -1 && AuthPlugin.getInstance().getConfig().getVKSettings().isEnabled()
					&& vkConfirmationEnabled) {
				Auth.addEntryAccount(this, vkId);
				return SessionResult.NEED_VK_CONFIRM;
			}
			if (getGoogleKey() != null && !getGoogleKey().isEmpty()
					&& AuthPlugin.getInstance().getConfig().getGoogleAuthenticatorSettings().isEnabled()) {
				Auth.addGoogleAuthAccount(this);
				return SessionResult.NEED_GOOGLE_CODE;
			}
			return SessionResult.LOGIN_SUCCESS;
		}
		return SessionResult.LOGIN_WRONG_PASSWORD;
	}

	@Override
	public KickResult kick(String reason) {
		ProxiedPlayer p = ProxyServer.getInstance().getPlayer(getUniqueId());
		if (p == null)
			return KickResult.PLAYER_OFFLINE;
		p.disconnect(TextComponent.fromLegacyText(reason));
		return KickResult.KICKED;
	}

	@Override
	public SessionResult logout(long sessionDurability) {
		if (!isSessionActive(sessionDurability))
			return SessionResult.LOGOUT_FAILED_NOT_LOGGED_IN;

		setLastSessionStart(0);
		return SessionResult.LOGOUT_SUCCESS;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isSessionActive(long sessionDurability) {
		ProxiedPlayer proxiedPlayer = identifierType.getPlayer(getId());
		if (proxiedPlayer == null)
			return (getLastSessionStart() + sessionDurability >= System.currentTimeMillis());
		return proxiedPlayer.getAddress().getHostString().equals(getLastIpAddress())
				&& (getLastSessionStart() + sessionDurability >= System.currentTimeMillis());
	}

	@Override
	public HashType getHashType() {
		return hashType;
	}

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
	public Integer getVKId() {
		return vkId;
	}

	@Override
	public void setVKId(Integer id) {
		this.vkId = id;
	}

	@Override
	public String getLastIpAddress() {
		return lastIpAddress;
	}

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
	public int compareTo(BungeeAccount o) {
		return name.compareTo(o.getName());
	}

	@Override
	public String getGoogleKey() {
		return googleKey;
	}

	@Override
	public void setGoogleKey(String googleKey) {
		this.googleKey = googleKey;
	}

	@Override
	public boolean isVKConfirmationEnabled() {
		return vkConfirmationEnabled;
	}

	@Override
	public void setVkConfirmationEnabled(boolean vkConfirmationEnabled) {
		this.vkConfirmationEnabled = vkConfirmationEnabled;
	}

}