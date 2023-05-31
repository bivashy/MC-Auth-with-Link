package me.mastercapexd.auth.crypto;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import com.bivashy.auth.api.crypto.HashInput;
import com.bivashy.auth.api.crypto.HashedPassword;

public class MessageDigestCryptoProvider extends BaseCryptoProvider {
    private static final String FORMAT = "%032x";
    private final MessageDigest digest;

    public MessageDigestCryptoProvider(String identifier, MessageDigest digest) {
        super(identifier);
        this.digest = digest;
    }

    @Override
    protected HashedPassword hashInput(HashInput input, String salt) {
        return HashedPassword.of(hash(input.getRawInput()), salt, this);
    }

    // We are using this hashing for backward compatibility
    private String hash(String input) {
        return String.format(FORMAT, new BigInteger(1, digest.digest(input.getBytes(StandardCharsets.UTF_8))));
    }
}
