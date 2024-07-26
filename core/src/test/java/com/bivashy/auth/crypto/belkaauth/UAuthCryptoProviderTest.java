package com.bivashy.auth.crypto.belkaauth;

import com.bivashy.auth.crypto.CryptoProviderTest;

import me.mastercapexd.auth.crypto.belkaauth.UAuthCryptoProvider;

public class UAuthCryptoProviderTest extends CryptoProviderTest {
    public UAuthCryptoProviderTest() {
        super(new UAuthCryptoProvider(),
                new String[]{"e5a05a7717d53b79586a7093540fd6fae3674a33", "176dfbe658d17a3335f03fc80b86bb3819d69c51",
                        "091019928aa81103a9806c84644f691a1417b219", "7b6654c454e15624698b5e582cfa1b08a42a781b"});
    }
}
