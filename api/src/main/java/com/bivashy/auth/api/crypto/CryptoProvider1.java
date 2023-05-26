package com.bivashy.auth.api.crypto;

public interface CryptoProvider1 {
    HashedPassword hash(HashInput input);

    boolean matches(HashInput input, String hashedInput);

    String getIdentifier();
}
