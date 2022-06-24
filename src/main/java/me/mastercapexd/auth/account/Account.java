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
import me.mastercapexd.auth.proxy.player.ProxyPlayer;

public interface Account {

    String getId();

    IdentifierType getIdentifierType();

    HashType getHashType();

    void setHashType(HashType hashType);

    UUID getUniqueId();

    String getName();

    String getPasswordHash();

    void setPasswordHash(String passwordHash);

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

    long getLastSessionStart();

    void setLastSessionStart(long currentTimeMillis);

    int getCurrentConfigurationAuthenticationStepCreatorIndex();

    void setCurrentConfigurationAuthenticationStepCreatorIndex(int index);

    AuthenticationStep getCurrentAuthenticationStep();

    boolean nextAuthenticationStep(AuthenticationStepContext stepContext);

    default void logout(long sessionDurability) {
        if (!isSessionActive(sessionDurability))
            return;
        setLastSessionStart(0);
    }

    default boolean isSessionActive(long sessionDurability) {
        Optional<ProxyPlayer> proxiedPlayer = getPlayer();
        long sessionEndTime = getLastSessionStart() + sessionDurability;
        if (!proxiedPlayer.isPresent())
            return (sessionEndTime >= System.currentTimeMillis());
        return proxiedPlayer.get().getRemoteAddress().getHostString().equals(getLastIpAddress()) && (sessionEndTime >= System.currentTimeMillis());
    }

    default KickResult kick(String reason) {
        Optional<ProxyPlayer> proxyPlayer = getPlayer();
        if (!proxyPlayer.isPresent())
            return KickResult.PLAYER_OFFLINE;
        proxyPlayer.get().disconnect(reason);
        return KickResult.KICKED;
    }

    default Optional<ProxyPlayer> getPlayer() {
        return getIdentifierType().getPlayer(getId());
    }

    default boolean isRegistered() {
        return getPasswordHash() != null;
    }
}