package me.mastercapexd.auth.account;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import me.mastercapexd.auth.HashType;
import me.mastercapexd.auth.IdentifierType;
import me.mastercapexd.auth.link.user.AccountLinkAdapter;
import me.mastercapexd.auth.link.user.LinkUser;
import me.mastercapexd.auth.link.user.info.identificator.LinkUserIdentificator;
import me.mastercapexd.auth.storage.model.AccountLink;
import me.mastercapexd.auth.storage.model.AuthAccount;
import me.mastercapexd.auth.storage.model.AuthAccountProvider;

public class AuthAccountAdapter extends AccountTemplate implements AuthAccountProvider {
    private final List<AccountLinkAdapter> linkUsers;
    private final AuthAccount authAccount;

    /**
     * Create {@link Account} from {@link AuthAccount}
     *
     * @param authAccount  - account that need to adapt
     * @param accountLinks - linked social {@link AccountLink} collection
     */
    public AuthAccountAdapter(AuthAccount authAccount, Collection<AccountLink> accountLinks) {
        this.authAccount = authAccount;
        this.linkUsers = accountLinks.stream().map(accountLink -> new AccountLinkAdapter(accountLink, this)).collect(Collectors.toList());
    }

    /**
     * Create {@link Account} from {@link AuthAccount}
     * <p>
     * This constructor will query {@link me.mastercapexd.auth.storage.model.AccountLink} from database, because of
     * {@link com.j256.ormlite.dao.ForeignCollection}
     *
     * @param authAccount - account that need to adapt
     * @see com.j256.ormlite.dao.ForeignCollection
     */
    public AuthAccountAdapter(AuthAccount authAccount) {
        this(authAccount, authAccount.getLinks());
    }

    @Override
    public AuthAccount getAuthAccount() {
        return authAccount;
    }

    @Override
    public CompletableFuture<Void> syncLinkAdaptersWithLinks() {
        return CompletableFuture.supplyAsync(() -> {
            for (AccountLinkAdapter linkUser : linkUsers) {
                String linkUserRawId = Optional.ofNullable(linkUser.getLinkUserInfo().getIdentificator())
                        .map(LinkUserIdentificator::asString)
                        .orElse(linkUser.getLinkType().getDefaultIdentificator().asString());
                boolean linkUserConfirmationEnabled = linkUser.getLinkUserInfo().isConfirmationEnabled();

                Optional<AccountLink> boundAccountLinkOptional = linkUser.getAccountLink();
                if (boundAccountLinkOptional.isPresent()) {
                    AccountLink boundAccountLink = boundAccountLinkOptional.get();
                    boundAccountLink.setLinkUserId(linkUserRawId);
                    boundAccountLink.setLinkEnabled(linkUserConfirmationEnabled);
                    try {
                        authAccount.getLinks().update(boundAccountLink);
                    } catch(SQLException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                authAccount.getLinks().add(new AccountLink(linkUser.getLinkType().getName(), linkUserRawId, linkUserConfirmationEnabled, authAccount));
            }
            return null;
        });
    }

    @Override
    public String getPlayerId() {
        return authAccount.getPlayerId();
    }

    @Override
    public IdentifierType getIdentifierType() {
        return authAccount.getPlayerIdType();
    }

    @Override
    public HashType getHashType() {
        return authAccount.getHashType();
    }

    @Override
    public void setHashType(HashType hashType) {
        authAccount.setHashType(hashType);
    }

    @Override
    public UUID getUniqueId() {
        return authAccount.getUniqueId();
    }

    @Override
    public String getName() {
        return authAccount.getPlayerName();
    }

    @Override
    public String getPasswordHash() {
        return authAccount.getPasswordHash();
    }

    @Override
    public void setPasswordHash(String passwordHash) {
        authAccount.setPasswordHash(passwordHash);
    }

    @Override
    public List<LinkUser> getLinkUsers() {
        return Collections.unmodifiableList(linkUsers);
    }

    @Override
    public void addLinkUser(LinkUser linkUser) {
        if (linkUser == null)
            return;
        if (linkUsers.stream().anyMatch(listLinkUser -> listLinkUser.getLinkType().equals(linkUser.getLinkType())))
            return;
        linkUsers.add(new AccountLinkAdapter(linkUser));
    }

    @Override
    public Optional<LinkUser> findFirstLinkUser(Predicate<LinkUser> filter) {
        return linkUsers.stream().filter(filter).findFirst().map(Function.identity());
    }

    @Override
    public long getLastQuitTimestamp() {
        return authAccount.getLastQuitTimestamp();
    }

    @Override
    public void setLastQuitTimestamp(long timestamp) {
        authAccount.setLastQuitTimestamp(timestamp);
    }

    @Override
    public String getLastIpAddress() {
        return authAccount.getLastIp();
    }

    @Override
    public void setLastIpAddress(String hostString) {
        authAccount.setLastIp(hostString);
    }

    @Override
    public long getLastSessionStartTimestamp() {
        return authAccount.getLastSessionStartTimestamp();
    }

    @Override
    public void setLastSessionStartTimestamp(long currentTimeMillis) {
        authAccount.setLastSessionStartTimestamp(currentTimeMillis);
    }

    @Override
    public int compareTo(AccountTemplate accountTemplate) {
        return accountTemplate.getName().compareTo(getName());
    }
}