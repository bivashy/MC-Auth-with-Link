package me.mastercapexd.auth.crypto;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import com.bivashy.auth.api.crypto.CryptoProvider;
import com.bivashy.auth.api.crypto.HashInput;
import com.bivashy.auth.api.crypto.HashedPassword;

public class MessageDigestCryptoProvider implements CryptoProvider {
    private static final String FORMAT = "%032x";
    private final String identifier;
    private final MessageDigest digest;

    public MessageDigestCryptoProvider(String identifier, MessageDigest digest) {
        this.identifier = identifier;
        this.digest = digest;
    }

    @Override
    public HashedPassword hash(HashInput input) {
        return HashedPassword.of(hash(input.getRawInput()), this);
    }

    @Override
    public boolean matches(HashInput input, HashedPassword password) {
        return MessageDigest.isEqual(hash(input.getRawInput()).getBytes(StandardCharsets.UTF_8), password.getHash().getBytes(StandardCharsets.UTF_8));
    }

    // We are using this hashing for backward compatibility
    private String hash(String input) {
        return String.format(FORMAT, new BigInteger(1, digest.digest(input.getBytes(StandardCharsets.UTF_8))));
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }
}
