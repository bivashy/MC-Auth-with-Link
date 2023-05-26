package com.bivashy.auth.api.bucket;

import java.util.Optional;

import com.bivashy.auth.api.crypto.CryptoProvider;

public interface CryptoProviderBucket {
    Optional<CryptoProvider> findCryptoProvider(String identifier);

    void addCryptoProvider(CryptoProvider cryptoProvider);
}
