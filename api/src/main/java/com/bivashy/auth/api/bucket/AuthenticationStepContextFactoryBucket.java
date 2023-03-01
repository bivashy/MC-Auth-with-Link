package com.bivashy.auth.api.bucket;

import java.util.Map;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.factory.AuthenticationStepContextFactory;
import com.bivashy.auth.api.step.AuthenticationStepContext;

public interface AuthenticationStepContextFactoryBucket {
    Map<String, AuthenticationStepContextFactory> getMap();

    AuthenticationStepContextFactory put(String key, AuthenticationStepContextFactory value);

    AuthenticationStepContextFactory remove(String key);

    AuthenticationStepContextFactory get(String key);

    AuthenticationStepContextFactory getOrDefault(String key, AuthenticationStepContextFactory def);

    boolean containsKey(String key);

    AuthenticationStepContext createContext(Account account);

    AuthenticationStepContext createContext(String stepName, Account account);
}
