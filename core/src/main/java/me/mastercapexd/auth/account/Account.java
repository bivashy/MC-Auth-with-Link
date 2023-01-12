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
    @Deprecated
    default String getId(){
        return getPlayerId();
    }

    String getPlayerId();

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

    @Deprecated
    default long getLastQuitTime(){
        return getLastQuitTimestamp();
    }

    @Deprecated
    default void setLastQuitTime(long time){
        setLastQuitTimestamp(time);
    }

    long getLastQuitTimestamp();

    void setLastQuitTimestamp(long timestamp);

    String getLastIpAddress();

    void setLastIpAddress(String hostString);

    @Deprecated
    default long getLastSessionStart(){
        return getLastSessionStartTimestamp();
    }

    @Deprecated
    default void setLastSessionStart(long currentTimeMillis){
        setLastSessionStartTimestamp(currentTimeMillis);
    }

    long getLastSessionStartTimestamp();

    void setLastSessionStartTimestamp(long timestamp);

    @Deprecated
    default int getCurrentConfigurationAuthenticationStepCreatorIndex(){
        return getCurrentAuthenticationStepCreatorIndex();
    }

    @Deprecated
    default void setCurrentConfigurationAuthenticationStepCreatorIndex(int index){
        setCurrentAuthenticationStepCreatorIndex(index);
    }

    int getCurrentAuthenticationStepCreatorIndex();

    void setCurrentAuthenticationStepCreatorIndex(int index);

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
        return proxiedPlayer.map(proxyPlayer -> proxyPlayer.getPlayerIp().equals(getLastIpAddress()) && (sessionEndTime >= System.currentTimeMillis()))
                .orElseGet(() -> (sessionEndTime >= System.currentTimeMillis()));
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