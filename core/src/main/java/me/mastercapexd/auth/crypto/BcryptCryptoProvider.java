package me.mastercapexd.auth.crypto;

import org.mindrot.jbcrypt.BCrypt;

import com.bivashy.auth.api.crypto.CryptoProvider;
import com.bivashy.auth.api.crypto.HashInput;
import com.bivashy.auth.api.crypto.HashedPassword;

public class BcryptCryptoProvider implements CryptoProvider {
    @Override
    public HashedPassword hash(HashInput input) {
        String salt = BCrypt.gensalt();
        return HashedPassword.of(BCrypt.hashpw(input.getRawInput(), salt), salt, this);
    }

    @Override
    public boolean matches(HashInput input, HashedPassword password) {
        return BCrypt.checkpw(input.getRawInput(), password.getHash());
    }

    @Override
    public String getIdentifier() {
        return "BCRYPT";
    }
}
