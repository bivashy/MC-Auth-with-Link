package me.mastercapexd.auth.account;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import me.mastercapexd.auth.HashType;
import me.mastercapexd.auth.IdentifierType;
import me.mastercapexd.auth.link.user.LinkUser;

public class DefaultAccount extends AbstractAccount {
    private final List<LinkUser> linkUsers = new ArrayList<>();
    private final String id;
    private final IdentifierType identifierType;
    private final UUID uniqueId;
    private final String name;
    private HashType hashType;
    private String passwordHash, lastIpAddress;
    private long lastQuitTime, lastSessionStart;

    public DefaultAccount(String id, IdentifierType identifierType, UUID uniqueId, String name) {
        this.id = id;
        this.identifierType = identifierType;
        this.uniqueId = uniqueId;
        this.name = name;
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
}