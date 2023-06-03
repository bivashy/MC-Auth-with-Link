package com.bivashy.auth.crypto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.*;

import com.bivashy.auth.api.crypto.CryptoProvider;
import com.bivashy.auth.api.crypto.HashInput;
import com.bivashy.auth.api.crypto.HashedPassword;

public abstract class CryptoProviderTest {
    private static final String[] PASSWORDS = {"password", "PassWord1", "&%^$&#te$t)_@", "âË_3(íù*"};
    private static final String[] RUNTIME_PASSWORDS = {"test", "TesT1", "%&#@%Y@#+Wp)_#", "Ûïé1&?+A"};
    private static final String FAKE_PASSWORD = "123";
    private CryptoProvider cryptoProvider;
    private Map<String, HashedPassword> hashes;

    public CryptoProviderTest(CryptoProvider cryptoProvider, String[] hashedPasswords) {
        this.cryptoProvider = cryptoProvider;
        hashes = IntStream.range(0, PASSWORDS.length)
                .boxed()
                .collect(Collectors.toMap(index -> PASSWORDS[index], index -> HashedPassword.of(hashedPasswords[index], cryptoProvider)));
    }

    @Test
    public void testHashes() {
        for (String password : hashes.keySet())
            assertTrue(doesPasswordMatch(password, hashes.get(password)),
                    "Password " + password + " doesn't match with CryptoProvider: " + cryptoProvider.getIdentifier());
    }

    @Test
    public void testRuntimePasswords() {
        HashedPassword fakePassword = cryptoProvider.hash(HashInput.of(FAKE_PASSWORD));
        for (String password : RUNTIME_PASSWORDS) {
            HashedPassword hashedPassword = cryptoProvider.hash(HashInput.of(password));
            assertTrue(doesPasswordMatch(password, hashedPassword),
                    "Runtime password " + password + " doesn't match with itself. CryptoProvider: " + cryptoProvider.getIdentifier());
            assertFalse(doesPasswordMatch(password, fakePassword),
                    "Runtime password " + password + " does match with wrong password. CryptoProvider: " + cryptoProvider.getIdentifier());
        }
    }

    private boolean doesPasswordMatch(String password, HashedPassword hashedPassword) {
        return cryptoProvider.matches(HashInput.of(password), hashedPassword);
    }
}
