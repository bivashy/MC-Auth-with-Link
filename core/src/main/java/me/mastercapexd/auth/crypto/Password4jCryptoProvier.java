package me.mastercapexd.auth.crypto;

import com.bivashy.auth.api.crypto.CryptoProvider;
import com.bivashy.auth.api.crypto.HashInput;
import com.bivashy.auth.api.crypto.HashedPassword;
import com.password4j.Hash;
import com.password4j.HashBuilder;
import com.password4j.HashChecker;
import com.password4j.Password;

public abstract class Password4jCryptoProvier implements CryptoProvider {
    private final String identifier;

    public Password4jCryptoProvier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public HashedPassword hash(HashInput input) {
        Hash result = build(Password.hash(input.getRawInput()).addRandomSalt());
        return HashedPassword.of(result.getResult(), result.getSalt(), this);
    }

    @Override
    public boolean matches(HashInput input, HashedPassword password) {
        return build(Password.check(input.getRawInput(), password.getHash()));
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    public abstract Hash build(HashBuilder builder);

    public abstract boolean build(HashChecker hashChecker);
}
