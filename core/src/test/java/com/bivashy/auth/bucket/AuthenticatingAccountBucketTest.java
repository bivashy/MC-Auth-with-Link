package com.bivashy.auth.bucket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.bucket.AuthenticatingAccountBucket;

import me.mastercapexd.auth.bucket.BaseAuthenticatingAccountBucket;

@ExtendWith(MockitoExtension.class)
public class AuthenticatingAccountBucketTest {
    private static final String ACCOUNT_ID = "Test";
    @Mock
    private Account account;
    private AuthenticatingAccountBucket bucket;

    @BeforeEach
    public void setup() {
        bucket = new BaseAuthenticatingAccountBucket(AuthPlugin.instance());
    }

    @Test
    public void shouldBeEmptyBucket() {
        when(account.getPlayerId()).thenReturn(ACCOUNT_ID);

        assertEquals(0, bucket.getAccountIdEntries().size());
        assertFalse(bucket.isAuthenticating(account));
        assertFalse(bucket.getAuthenticatingAccount(account).isPresent());
        assertNull(bucket.getAuthenticatingAccountNullable(account));
        assertEquals(0, bucket.getEnterTimestampOrZero(account));
        assertFalse(bucket.getEnterTimestamp(account).isPresent());
    }

    @Test
    public void shouldAddIntoBucket() {
        when(account.getPlayerId()).thenReturn(ACCOUNT_ID);

        bucket.addAuthenticatingAccount(account);
        assertEquals(1, bucket.getAccountIdEntries().size());
        assertTrue(bucket.isAuthenticating(account));
        assertTrue(bucket.getAuthenticatingAccount(account).isPresent());
        assertEquals(account, bucket.getAuthenticatingAccountNullable(account));
        assertNotEquals(0, bucket.getEnterTimestampOrZero(account));
        assertTrue(bucket.getEnterTimestamp(account).isPresent());
    }
}
