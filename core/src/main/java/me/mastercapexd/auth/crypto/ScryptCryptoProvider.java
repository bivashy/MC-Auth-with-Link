package me.mastercapexd.auth.crypto;

import com.password4j.Hash;
import com.password4j.HashBuilder;
import com.password4j.HashChecker;

public class ScryptCryptoProvider extends Password4jCryptoProvier {
    public ScryptCryptoProvider() {
        super("SCRYPT");
    }

    @Override
    public Hash build(HashBuilder builder) {
        return builder.withScrypt();
    }

    @Override
    public boolean build(HashChecker hashChecker) {
        return hashChecker.withScrypt();
    }
}
