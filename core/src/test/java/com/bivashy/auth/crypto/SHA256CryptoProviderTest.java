package com.bivashy.auth.crypto;

import me.mastercapexd.auth.crypto.MessageDigestCryptoProvider;
import me.mastercapexd.auth.util.HashUtils;

public class SHA256CryptoProviderTest extends CryptoProviderTest {
    public SHA256CryptoProviderTest() {
        super(new MessageDigestCryptoProvider("SHA256", HashUtils.getSHA256()),
                new String[]{"5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8",
                        "c04265d72b749debd67451c083785aa572742e3222e86884de16485fa14b55e7", "638a9bdb7b878b4c5f0c48f4443e6c2bacddcdebfd1c48f082484ad94c099f7"
                        , "55f13bb240a85bbf4f2483525d80188d2cedc45f8b6775b6668fdc9e3982f50e"});
    }
}
