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

    default HashedPassword withSalt(String salt) {
        return HashedPassword.of(getHash(), salt, getCryptoProvider());
    }

    String getHash();

    String getSalt();

    CryptoProvider getCryptoProvider();
}
