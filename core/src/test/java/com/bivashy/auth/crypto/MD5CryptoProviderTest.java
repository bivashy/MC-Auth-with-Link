package com.bivashy.auth.crypto;

import me.mastercapexd.auth.crypto.MessageDigestCryptoProvider;
import me.mastercapexd.auth.util.HashUtils;

public class MD5CryptoProviderTest extends CryptoProviderTest {

    public MD5CryptoProviderTest() {
        super(new MessageDigestCryptoProvider("MD5", HashUtils.getMD5()),
                new String[]{
                        "5f4dcc3b5aa765d61d8327deb882cf99",
                        "f2126d405f46ed603ff5b2950f062c96",
                        "fcf970e38e0c903e8d4f288de0391ccc",
                        "d1accd961cb7b688c87278191c1dfed3"});
    }

}
