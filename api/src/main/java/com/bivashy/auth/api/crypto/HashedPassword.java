package com.bivashy.auth.api.crypto;

public interface HashedPassword {
    static HashedPassword of(String hash, String salt, CryptoProvider cryptoProvider) {
        return new HashedPassword() {
            @Override
            public String getHash() {
                return hash;
            }

            @Override
            public String getSalt() {
                return salt;
            }

            @Override
            public CryptoProvider getCryptoProvider() {
                return cryptoProvider;
            }
        };
    }

    static HashedPassword of(String hash, CryptoProvider cryptoProvider) {
        return of(hash, null, cryptoProvider);
    }

    String getHash();

    String getSalt();

    CryptoProvider getCryptoProvider();
}
