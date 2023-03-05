package me.mastercapexd.auth.bucket;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.bucket.AuthenticationStepContextFactoryBucket;
import com.bivashy.auth.api.factory.AuthenticationStepContextFactory;
import com.bivashy.auth.api.step.AuthenticationStepContext;

import me.mastercapexd.auth.step.context.BaseAuthenticationStepContext;

public class BaseAuthenticationStepContextFactoryBucket implements AuthenticationStepContextFactoryBucket {
    private final Map<String, AuthenticationStepContextFactory> authenticationStepContextFactories = new HashMap<>();
    private final List<String> stepNames;

    public BaseAuthenticationStepContextFactoryBucket(List<String> stepNames) {
        this.stepNames = stepNames;
    }

    public Map<String, AuthenticationStepContextFactory> getMap() {
        return Collections.unmodifiableMap(authenticationStepContextFactories);
    }

    public AuthenticationStepContextFactory put(String key, AuthenticationStepContextFactory value) {
        return authenticationStepContextFactories.put(key, value);
    }

    public AuthenticationStepContextFactory remove(String key) {
        return authenticationStepContextFactories.remove(key);
    }

    public AuthenticationStepContextFactory get(String key) {
        return authenticationStepContextFactories.get(key);
    }

    public AuthenticationStepContextFactory getOrDefault(String key, AuthenticationStepContextFactory def) {
        return authenticationStepContextFactories.getOrDefault(key, def);
    }

    public boolean containsKey(String key) {
        return authenticationStepContextFactories.containsKey(key);
    }

    public AuthenticationStepContext createContext(Account account) {
        String stepName = stepNames.get(0); // Use first stepName by default
        if (stepNames.size() > account.getCurrentAuthenticationStepCreatorIndex()) // If current
            // authentication step
            // index not out of
            // bounds stepNames size
            stepName = stepNames.get(account.getCurrentAuthenticationStepCreatorIndex()); // use this step
        // name
        return createContext(stepName, account);
    }

    public AuthenticationStepContext createContext(String stepName, Account account) {
        return authenticationStepContextFactories.getOrDefault(stepName, AuthenticationStepContextFactory.of(new BaseAuthenticationStepContext(account)))
                .createContext(account);
    }
}
