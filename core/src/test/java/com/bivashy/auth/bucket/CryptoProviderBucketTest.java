package com.bivashy.auth.bucket;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bivashy.auth.api.bucket.Bucket;
import com.bivashy.auth.api.bucket.CryptoProviderBucket;
import com.bivashy.auth.api.crypto.CryptoProvider;

import me.mastercapexd.auth.bucket.BaseCryptoProviderBucket;

@ExtendWith(MockitoExtension.class)
public class CryptoProviderBucketTest extends SimpleBucketTest<CryptoProvider> {

    private CryptoProviderBucket bucket;
    @Mock
    private CryptoProvider cryptoProvider;

    @BeforeEach
    public void setup() {
        bucket = new BaseCryptoProviderBucket();
    }

    @Override
    Bucket<CryptoProvider> getBucket() {
        return bucket;
    }

    @Override
    CryptoProvider element() {
        return cryptoProvider;
    }

}
