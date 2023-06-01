package me.mastercapexd.auth.crypto.belkaauth;

import com.bivashy.auth.api.crypto.CryptoProvider;
import com.bivashy.auth.api.crypto.HashInput;
import com.bivashy.auth.api.crypto.HashedPassword;

public class UAuthCryptoProvider implements CryptoProvider {
    private static final String SALT = "saharPidor";

    @Override
    public HashedPassword hash(HashInput input) {
        return HashedPassword.of(UAuthUtil.getHash(SALT, input.getRawInput()), SALT, this);
    }

    @Override
    public boolean matches(HashInput input, HashedPassword password) {
        return hash(input).getHash().equals(password.getHash());
    }

    @Override
    public String getIdentifier() {
        return "UAUTH_UNSAFE";
    }
}
