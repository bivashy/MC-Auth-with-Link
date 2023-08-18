package com.bivashy.auth.api.bucket;

import java.util.Optional;

import com.bivashy.auth.api.crypto.CryptoProvider;

public interface CryptoProviderBucket extends Bucket<CryptoProvider> {

    @Deprecated
    default Optional<CryptoProvider> findCryptoProvider(String identifier) {
        return findFirstByValue(CryptoProvider::getIdentifier, identifier);
    }

    @Deprecated
    default void addCryptoProvider(CryptoProvider cryptoProvider) {
        modifiable().add(cryptoProvider);
    }

}
