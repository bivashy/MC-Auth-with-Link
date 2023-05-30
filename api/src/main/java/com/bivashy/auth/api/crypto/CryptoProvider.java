package com.bivashy.auth.api.crypto;

public interface CryptoProvider {
    HashedPassword hash(HashInput input);

    boolean matches(HashInput input, HashedPassword password);

    String getIdentifier();
}
