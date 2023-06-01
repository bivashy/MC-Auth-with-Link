package me.mastercapexd.auth.crypto.authme;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import com.bivashy.auth.api.crypto.CryptoProvider;
import com.bivashy.auth.api.crypto.HashInput;
import com.bivashy.auth.api.crypto.HashedPassword;

import me.mastercapexd.auth.util.HashUtils;

public class AuthMeSha256CryptoProvider implements CryptoProvider {
    private static final MessageDigest MESSAGE_DIGEST = HashUtils.getSHA256();
    private static final int SALT_LENGTH = 16;

    @Override
    public HashedPassword hash(HashInput input) {
        String salt = SaltUtil.generateHex(SALT_LENGTH);
        return hash(input.getRawInput(), salt);
    }

    @Override
    public boolean matches(HashInput input, HashedPassword password) {
        String hash = password.getHash();
        String[] line = hash.split("\\$");
        String lineSalt = line[2];
        String hashedPassword = hash(input.getRawInput(), lineSalt).getHash();
        return line.length == 4 && MessageDigest.isEqual(hashedPassword.getBytes(StandardCharsets.UTF_8),
                password.getHash().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String getIdentifier() {
        return "AUTHME_SHA256";
    }

    private HashedPassword hash(String input, String salt) {
        String hash = HashUtils.hashText(HashUtils.hashText(input, MESSAGE_DIGEST) + salt, MESSAGE_DIGEST);
        return HashedPassword.of("$SHA$" + salt + "$" + hash, salt, this);
    }
}
