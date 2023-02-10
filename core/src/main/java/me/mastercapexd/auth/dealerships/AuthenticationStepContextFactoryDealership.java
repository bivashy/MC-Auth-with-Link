package me.mastercapexd.auth.dealerships;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.context.DefaultAuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.context.factory.AuthenticationStepContextFactory;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class AuthenticationStepContextFactoryDealership {
    private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();
    private final Map<String, AuthenticationStepContextFactory> authenticationStepContextFactories = new HashMap<>();

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
        List<String> stepNames = PLUGIN.getConfig().getAuthenticationSteps();
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
        return authenticationStepContextFactories.getOrDefault(stepName, AuthenticationStepContextFactory.of(new DefaultAuthenticationStepContext(account)))
                .createContext(account);
    }
}
