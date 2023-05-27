package me.mastercapexd.auth.bucket;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bivashy.auth.api.bucket.CryptoProviderBucket;
import com.bivashy.auth.api.crypto.CryptoProvider1;

public class BaseCryptoProviderBucket implements CryptoProviderBucket {
    private final List<CryptoProvider1> cryptoProviders = new ArrayList<>();

    @Override
    public Optional<CryptoProvider1> findCryptoProvider(String identifier) {
        return cryptoProviders.stream().filter(provider -> provider.getIdentifier().equals(identifier)).findFirst();
    }

    @Override
    public void addCryptoProvider(CryptoProvider1 cryptoProvider) {
        cryptoProviders.add(cryptoProvider);
    }
}
