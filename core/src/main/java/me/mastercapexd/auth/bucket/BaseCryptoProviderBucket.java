package me.mastercapexd.auth.bucket;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bivashy.auth.api.bucket.CryptoProviderBucket;
import com.bivashy.auth.api.crypto.CryptoProvider;

public class BaseCryptoProviderBucket implements CryptoProviderBucket {
    private final List<CryptoProvider> cryptoProviders = new ArrayList<>();

    @Override
    public Optional<CryptoProvider> findCryptoProvider(String identifier) {
        return cryptoProviders.stream().filter(provider -> provider.getIdentifier().equals(identifier)).findFirst();
    }

    @Override
    public void addCryptoProvider(CryptoProvider cryptoProvider) {
        cryptoProviders.add(cryptoProvider);
    }
}
