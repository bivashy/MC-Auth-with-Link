package com.bivashy.auth.account;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.UUID;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.crypto.CryptoProvider;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.type.IdentifierType;

import me.mastercapexd.auth.account.factory.AuthAccountFactory;

@ExtendWith(MockitoExtension.class)
public class AuthAccountFactoryTest {
    private static final String ACCOUNT_ID = "player";
    private static final IdentifierType ACCOUNT_ID_TYPE = IdentifierType.NAME;
    private static final String ACCOUNT_NAME = "Player";
    private static final UUID ACCOUNT_UUID = UUID.randomUUID();
    private static final String ACCOUNT_LAST_IP = "0.0.0.0";
    private final AuthAccountFactory factory = new AuthAccountFactory();
    @Mock
    private CryptoProvider cryptoProvider;

    @Test
    public void testUnregisteredAccount() {
        Account account = factory.createAccount(ACCOUNT_ID, ACCOUNT_ID_TYPE, ACCOUNT_UUID, ACCOUNT_NAME, cryptoProvider, null, ACCOUNT_LAST_IP);

        assertNotNull(account);

        assertEquals(ACCOUNT_ID, account.getPlayerId());
        assertEquals(ACCOUNT_NAME, account.getName());
        assertEquals(ACCOUNT_UUID, account.getUniqueId());
        assertEquals(ACCOUNT_LAST_IP, account.getLastIpAddress());
        assertEquals(cryptoProvider, account.getCryptoProvider());

        assertFalse(account.isRegistered());

        long sessionDuration = Duration.of(5, ChronoUnit.MINUTES).toMillis();
        assertFalse(account.isSessionActive(sessionDuration));

        Collection<LinkType> linkTypes = AuthPlugin.instance().getLinkTypeProvider().getLinkTypes();
        assertEquals(linkTypes.size(), account.getLinkUsers().size(), "Account should have same amount of link users as link types");
    }
}
