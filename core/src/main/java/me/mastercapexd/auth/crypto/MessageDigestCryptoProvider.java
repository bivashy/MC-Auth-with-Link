package me.mastercapexd.auth.crypto;

import java.security.MessageDigest;

import com.bivashy.auth.api.crypto.HashInput;
import com.bivashy.auth.api.crypto.HashedPassword;
import me.mastercapexd.auth.util.HashUtils;

public class MessageDigestCryptoProvider extends BaseCryptoProvider {
    private final MessageDigest digest;

    public MessageDigestCryptoProvider(String identifier, MessageDigest digest) {
        super(identifier);
        this.digest = digest;
    }

    @Override
    protected HashedPassword hashInput(HashInput input, String salt) {
        return HashedPassword.of(HashUtils.hashText(input.getRawInput(), digest), salt, this);
    }
}
