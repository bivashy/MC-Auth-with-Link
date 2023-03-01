package com.bivashy.auth.api.account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.LinkUser;
import com.bivashy.auth.api.link.user.LinkUserTemplate;
import com.bivashy.auth.api.link.user.info.impl.LinkUserInfoTemplate;
import com.bivashy.auth.api.model.PlayerIdSupplier;
import com.bivashy.auth.api.server.player.ServerPlayer;
import com.bivashy.auth.api.step.AuthenticationStep;
import com.bivashy.auth.api.step.AuthenticationStepContext;
import com.bivashy.auth.api.type.HashType;
import com.bivashy.auth.api.type.IdentifierType;
import com.bivashy.auth.api.type.KickResultType;

public interface Account extends PlayerIdSupplier {
    @Deprecated
    default String getId() {
        return getPlayerId();
    }

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
     * @param filter filter
     * @return ILinkUser that fits filter
     */
    Optional<LinkUser> findFirstLinkUser(Predicate<LinkUser> filter);

    default LinkUser findFirstLinkUserOrCreate(Predicate<LinkUser> filter, LinkUser def) {
        return findFirstLinkUser(filter).orElseGet(() -> {
            addLinkUser(def);
            return def;
        });
    }

    default LinkUser findFirstLinkUserOrNew(Predicate<LinkUser> filter, LinkType linkType) {
        return findFirstLinkUserOrCreate(filter, LinkUserTemplate.of(linkType, this, new LinkUserInfoTemplate(null)));
    }

    @Deprecated
    default long getLastQuitTime() {
        return getLastQuitTimestamp();
    }

    @Deprecated
    default void setLastQuitTime(long time) {
        setLastQuitTimestamp(time);
    }

    long getLastQuitTimestamp();

    void setLastQuitTimestamp(long timestamp);

    String getLastIpAddress();

    void setLastIpAddress(String hostString);

    @Deprecated
    default long getLastSessionStart() {
        return getLastSessionStartTimestamp();
    }

    @Deprecated
    default void setLastSessionStart(long currentTimeMillis) {
        setLastSessionStartTimestamp(currentTimeMillis);
    }

    long getLastSessionStartTimestamp();

    void setLastSessionStartTimestamp(long timestamp);

    @Deprecated
    default int getCurrentConfigurationAuthenticationStepCreatorIndex() {
        return getCurrentAuthenticationStepCreatorIndex();
    }

    @Deprecated
    default void setCurrentConfigurationAuthenticationStepCreatorIndex(int index) {
        setCurrentAuthenticationStepCreatorIndex(index);
    }

    int getCurrentAuthenticationStepCreatorIndex();

    void setCurrentAuthenticationStepCreatorIndex(int index);

    AuthenticationStep getCurrentAuthenticationStep();

    void nextAuthenticationStep(AuthenticationStepContext stepContext);

    default void logout(long sessionDurability) {
        if (!isSessionActive(sessionDurability))
            return;
        setLastSessionStartTimestamp(0);
    }

    boolean isSessionActive(long sessionDurability);

    default KickResultType kick(String reason) {
        Optional<ServerPlayer> serverPlayer = getPlayer();
        if (!serverPlayer.isPresent())
            return KickResultType.PLAYER_OFFLINE;
        serverPlayer.get().disconnect(reason);
        return KickResultType.KICKED;
    }

    default Optional<ServerPlayer> getPlayer() {
        return getIdentifierType().getPlayer(getPlayerId());
    }

    default boolean isRegistered() {
        return getPasswordHash() != null;
    }
}