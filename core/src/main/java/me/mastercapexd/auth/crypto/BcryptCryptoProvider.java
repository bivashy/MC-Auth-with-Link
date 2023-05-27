package me.mastercapexd.auth.crypto;

import org.mindrot.jbcrypt.BCrypt;

import com.bivashy.auth.api.crypto.HashInput;
import com.bivashy.auth.api.crypto.HashedPassword;

public class BcryptCryptoProvider extends BaseCryptoProvider {
    public BcryptCryptoProvider() {
        super("BCRYPT");
    }

    @Override
    public boolean matches(HashInput input, HashedPassword password) {
        return BCrypt.checkpw(input.getRawInput(), password.getHash());
    }

    @Override
    protected HashedPassword hashInput(HashInput input, String salt) {
        return HashedPassword.of(BCrypt.hashpw(input.getRawInput(), salt), salt, this);
    }

    @Override
    protected String generateSalt() {
        return BCrypt.gensalt();
    }
}
