package me.mastercapexd.auth.bucket;

import java.util.ArrayList;

import com.bivashy.auth.api.bucket.CryptoProviderBucket;
import com.bivashy.auth.api.crypto.CryptoProvider;

public class BaseCryptoProviderBucket extends BaseListBucket<CryptoProvider> implements CryptoProviderBucket {

    public BaseCryptoProviderBucket() {
        super(new ArrayList<>());
    }

}
