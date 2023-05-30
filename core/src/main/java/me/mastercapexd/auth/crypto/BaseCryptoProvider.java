package me.mastercapexd.auth.crypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import com.bivashy.auth.api.crypto.CryptoProvider;
import com.bivashy.auth.api.crypto.HashInput;
import com.bivashy.auth.api.crypto.HashedPassword;

public abstract class BaseCryptoProvider implements CryptoProvider {
    private final String identifier;

    public BaseCryptoProvider(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public HashedPassword hash(HashInput input) {
        return hash(input, generateSalt());
    }

    protected HashedPassword hash(HashInput input, String salt) {
        String password = input.getRawInput();
        String hash = password + salt;
        for (int i = 0; i < input.getHashIteration(); i++) {
            hash = hashInput(HashInput.of(hash + password + salt), salt).getHash();
        }
        return HashedPassword.of(hash, salt, this);
    }

    protected abstract HashedPassword hashInput(HashInput input, String salt);

    @Override
    public boolean matches(HashInput input, HashedPassword password) {
        return MessageDigest.isEqual(hash(input, password.getSalt()).getHash().getBytes(StandardCharsets.UTF_8),
                password.getHash().getBytes(StandardCharsets.UTF_8));
    }

    protected String generateSalt() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }
}
