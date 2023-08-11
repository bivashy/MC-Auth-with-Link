package com.bivashy.auth.bucket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bivashy.auth.api.bucket.CryptoProviderBucket;
import com.bivashy.auth.api.crypto.CryptoProvider;

import me.mastercapexd.auth.bucket.BaseCryptoProviderBucket;

@ExtendWith(MockitoExtension.class)
public class CryptoProviderBucketTest {
    private static final String CRYPTO_NAME = "TEST";
    private CryptoProviderBucket bucket;
    @Mock
    private CryptoProvider cryptoProvider;

    @BeforeEach
    public void setup() {
        bucket = new BaseCryptoProviderBucket();
    }

    @Test
    public void shouldNotFindAnyElement() {
        assertFalse(bucket.findCryptoProvider(CRYPTO_NAME).isPresent());
        assertFalse(bucket.findCryptoProvider(null).isPresent());
        assertFalse(bucket.findCryptoProvider("").isPresent());
    }

    @Test
    public void shouldAddElement() {
        when(cryptoProvider.getIdentifier()).thenReturn(CRYPTO_NAME);

        assertFalse(bucket.findCryptoProvider(null).isPresent());
        assertFalse(bucket.findCryptoProvider("").isPresent());
        assertFalse(bucket.findCryptoProvider("test").isPresent());

        assertTrue(bucket.findCryptoProvider(CRYPTO_NAME).isPresent());
        assertEquals(cryptoProvider, bucket.findCryptoProvider(CRYPTO_NAME).get());
    }
}
