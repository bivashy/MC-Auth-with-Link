package me.mastercapexd.auth.crypto;

import com.password4j.Hash;
import com.password4j.HashBuilder;
import com.password4j.HashChecker;

public class Argon2CryptoProvider extends Password4jCryptoProvier {
    public Argon2CryptoProvider() {
        super("ARGON2");
    }

    @Override
    public Hash build(HashBuilder builder) {
        return builder.withArgon2();
    }

    @Override
    public boolean build(HashChecker hashChecker) {
        return hashChecker.withArgon2();
    }
}
