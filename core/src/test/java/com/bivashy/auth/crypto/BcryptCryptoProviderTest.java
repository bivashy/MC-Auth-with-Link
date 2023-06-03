package com.bivashy.auth.crypto;

import me.mastercapexd.auth.crypto.BcryptCryptoProvider;

public class BcryptCryptoProviderTest extends CryptoProviderTest {
    public BcryptCryptoProviderTest() {
        super(new BcryptCryptoProvider(),
                new String[]{"$2a$10$Cdbti2PxNxq1l/InGooHC.3YvCe1N1E.SXu4tdXuy.0Ycobjusqha", "$2a$10$Nu.feGGXRE8aGilSqpauR.tznTittcD14C4YIjM9sl9/aDf13C8ki",
                        "$2a$10$oSsiYlfXN9QYVe.Gv6HLEOZhP5CUksp7iTfulgEWeandcy4xfCr7W", "$2a$10$S8aTidKRg6D1r4V5w/UH3uZbbMTiSd7W7yx/98hN6nZU/bYCN43Da"});
    }
}
