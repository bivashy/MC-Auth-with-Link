package com.bivashy.auth.account;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.crypto.CryptoProvider;
import com.bivashy.auth.api.crypto.HashedPassword;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.LinkUser;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.type.IdentifierType;

import me.mastercapexd.auth.account.factory.AuthAccountFactory;

@ExtendWith(MockitoExtension.class)
public class AuthAccountTest {
    private static final String ACCOUNT_ID = "player";
    private static final IdentifierType ACCOUNT_ID_TYPE = IdentifierType.NAME;
    private static final String ACCOUNT_NAME = "Player";
    private static final UUID ACCOUNT_UUID = UUID.randomUUID();
    private final AuthAccountFactory factory = new AuthAccountFactory();
    private Account account;
    @Mock
    private CryptoProvider cryptoProvider;
    @Mock
    private LinkType linkType;
    @Mock
    private LinkUser linkUser;

    @BeforeEach
    public void setup() {
        account = factory.createAccount(ACCOUNT_ID, ACCOUNT_ID_TYPE, ACCOUNT_UUID, ACCOUNT_NAME, cryptoProvider, null, null);
    }

    @Test
    public void shouldBeUnregistered() {
        HashedPassword hashedPassword = account.getPasswordHash();
        assertNotNull(hashedPassword);
        assertEquals(cryptoProvider, hashedPassword.getCryptoProvider());
        assertNull(hashedPassword.getHash());
        assertNull(hashedPassword.getSalt());

        assertFalse(account.isRegistered());
    }

    @Test
    public void shouldBeRegistered() {
        HashedPassword hashedPassword = HashedPassword.of("test", cryptoProvider);
        account.setPasswordHash(hashedPassword);
        assertEquals(hashedPassword.getHash(), account.getPasswordHash().getHash());
        assertTrue(account.isRegistered());
    }

    @Test
    public void sessionShouldBeInactive() {
        long sessionDurationInMillis = Duration.of(5, ChronoUnit.MINUTES).toMillis();
        assertFalse(account.isSessionActive(sessionDurationInMillis));
        assertEquals(account.getLastSessionStartTimestamp(), 0);

        long sessionEndTimestamp = account.getLastSessionStartTimestamp() + sessionDurationInMillis;
        assertFalse(sessionEndTimestamp >= System.currentTimeMillis());

        account.setLastSessionStartTimestamp(System.currentTimeMillis() - sessionEndTimestamp - 1);
        assertFalse(account.isSessionActive(sessionDurationInMillis));
        assertTrue(account.isSessionActive(sessionDurationInMillis + Duration.ofSeconds(1).toMillis()));
    }

    @Test
    public void sessionShouldBeActive() {
        long currentTime = System.currentTimeMillis();
        account.setLastSessionStartTimestamp(currentTime);
        long sessionDurationInMillis = Duration.of(5, ChronoUnit.MINUTES).toMillis();
        assertTrue(account.isSessionActive(sessionDurationInMillis));
        assertEquals(account.getLastSessionStartTimestamp(), currentTime);

        long sessionEndTimestamp = account.getLastSessionStartTimestamp() + sessionDurationInMillis;
        assertTrue(sessionEndTimestamp >= System.currentTimeMillis());

        assertFalse(account.isSessionActive(-1));
    }

    @Test
    public void shouldAddLinkUser() {
        when(linkUser.getLinkType()).thenReturn(linkType);

        Optional<LinkUser> emptyOptionalLinkUser = account.findFirstLinkUser(user -> user.getLinkType().equals(linkType));
        assertFalse(emptyOptionalLinkUser.isPresent());

        int linkUsersCountBeforeAdd = account.getLinkUsers().size();

        account.addLinkUser(linkUser);

        assertEquals(linkUsersCountBeforeAdd + 1, account.getLinkUsers().size());

        Optional<LinkUser> foundLinkUser = account.findFirstLinkUser(user -> user.getLinkType().equals(linkType));
        assertTrue(foundLinkUser.isPresent());
        assertEquals(linkUser, foundLinkUser.get());

        LinkUser linkUserOrDefault = account.findFirstLinkUserOrCreate(user -> user.getLinkType().equals(linkType), null);
        assertNotNull(linkUserOrDefault);
        assertEquals(linkUser, linkUserOrDefault);

        LinkUser linkUserOrNew = account.findFirstLinkUserOrNew(user -> user.getLinkType().equals(linkType), linkType);
        assertNotNull(linkUserOrNew);
        assertEquals(linkUser, linkUserOrNew);
    }

    @Test
    public void shouldReturnDefaultLinkUser() {
        when(linkUser.getLinkType()).thenReturn(linkType);

        Optional<LinkUser> emptyOptionalLinkUser = account.findFirstLinkUser(user -> user.getLinkType().equals(linkType));
        assertFalse(emptyOptionalLinkUser.isPresent());

        int linkUsersCountBeforeAdd = account.getLinkUsers().size();

        LinkUser linkUserOrDefault = account.findFirstLinkUserOrCreate(user -> user.getLinkType().equals(linkType), linkUser);
        assertNotNull(linkUserOrDefault);
        assertEquals(linkUser, linkUserOrDefault);

        assertEquals(linkUsersCountBeforeAdd + 1, account.getLinkUsers().size());

        Optional<LinkUser> foundLinkUser = account.findFirstLinkUser(user -> user.getLinkType().equals(linkType));
        assertTrue(foundLinkUser.isPresent());
        assertEquals(linkUser, foundLinkUser.get());

        LinkUser linkUserOrNew = account.findFirstLinkUserOrNew(user -> user.getLinkType().equals(linkType), linkType);
        assertNotNull(linkUserOrNew);
        assertEquals(linkUser, linkUserOrNew);
    }

    @Test
    public void shouldCreateNewLinkUser() {
        LinkUserIdentificator userIdentificator = LinkUserIdentificator.of("testId");
        when(linkType.getDefaultIdentificator()).thenReturn(userIdentificator);

        Optional<LinkUser> emptyOptionalLinkUser = account.findFirstLinkUser(user -> user.getLinkType().equals(linkType));
        assertFalse(emptyOptionalLinkUser.isPresent());

        int linkUsersCountBeforeAdd = account.getLinkUsers().size();

        LinkUser linkUserOrNew = account.findFirstLinkUserOrNew(user -> user.getLinkType().equals(linkType), linkType);
        assertNotNull(linkUserOrNew);
        assertTrue(linkUserOrNew.isIdentifierDefaultOrNull());
        assertEquals(userIdentificator, linkUserOrNew.getLinkUserInfo().getIdentificator());

        assertEquals(linkUsersCountBeforeAdd + 1, account.getLinkUsers().size());

        Optional<LinkUser> foundLinkUser = account.findFirstLinkUser(user -> user.getLinkType().equals(linkType));
        assertTrue(foundLinkUser.isPresent());
        assertEquals(linkUserOrNew, foundLinkUser.get());
    }
}
