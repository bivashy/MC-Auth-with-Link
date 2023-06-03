package com.bivashy.auth.crypto.authme;

import com.bivashy.auth.crypto.CryptoProviderTest;

import me.mastercapexd.auth.crypto.authme.AuthMeSha256CryptoProvider;

public class AuthMeSha256CryptoProviderTest extends CryptoProviderTest {
    public AuthMeSha256CryptoProviderTest() {
        super(new AuthMeSha256CryptoProvider(),
                new String[]{"$SHA$472db2243f575240$67e36b33f2e64bde360356656e5b54c0bfb579d734cfde87285797dc8582514d",
                        "$SHA$6182b8ff2d95ecb1$6b6aeabe7d0c9db5e57de1c4b7cdd0a7430ba505a341bd859a1e996cf657d38c",
                        "$SHA$ad6c833adf91cf37$1689b301c70494bd84cb0e676614723bbe4f23dfc8d4af02f63959e35bab8b2c",
                        "$SHA$1fd935c82a23070f$09f13f4b32decd22aca034fe5050b4d423117e28903bc33cf5fbe7edacad96bb"});
    }
}
