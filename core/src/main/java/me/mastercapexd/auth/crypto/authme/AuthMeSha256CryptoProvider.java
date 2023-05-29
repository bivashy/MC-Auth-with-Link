package me.mastercapexd.auth.crypto.authme;

import java.security.MessageDigest;

import com.bivashy.auth.api.crypto.HashInput;
import com.bivashy.auth.api.crypto.HashedPassword;

import me.mastercapexd.auth.crypto.BaseCryptoProvider;
import me.mastercapexd.auth.util.HashUtils;

public class AuthMeSha256CryptoProvider extends BaseCryptoProvider {
    private static final MessageDigest MESSAGE_DIGEST = HashUtils.getSHA256();
    private static final int SALT_LENGTH = 16;

    public AuthMeSha256CryptoProvider() {
        super("AUTHME_SHA256");
    }

    @Override
    protected HashedPassword hashInput(HashInput input, String salt) {
        String hashedRawInput = HashUtils.hashText(HashUtils.hashText(input.getRawInput(), MESSAGE_DIGEST) + salt, MESSAGE_DIGEST);
        return HashedPassword.of("$SHA$" + salt + "$" + hashedRawInput, salt, this);
    }

    @Override
    public boolean matches(HashInput input, HashedPassword password) {
        String hash = password.getHash();
        String[] line = hash.split("\\$");
        String lineSalt = line[2];
        return line.length == 4 && super.matches(input, password.withSalt(lineSalt));
    }

    @Override
    protected String generateSalt() {
        return SaltUtil.generateHex(SALT_LENGTH);
    }
}
