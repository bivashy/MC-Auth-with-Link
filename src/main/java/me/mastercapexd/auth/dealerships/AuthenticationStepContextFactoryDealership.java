package me.mastercapexd.auth.dealerships;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.context.DefaultAuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.context.factory.AuthenticationStepContextFactory;
import me.mastercapexd.auth.proxy.ProxyPlugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthenticationStepContextFactoryDealership implements MapDealership<String, AuthenticationStepContextFactory> {
    private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();
    private final Map<String, AuthenticationStepContextFactory> authenticationStepContextFactories = new HashMap<>();

    @Override
    public Map<String, AuthenticationStepContextFactory> getMap() {
        return Collections.unmodifiableMap(authenticationStepContextFactories);
    }

    @Override
    public AuthenticationStepContextFactory put(String key, AuthenticationStepContextFactory value) {
        return authenticationStepContextFactories.put(key, value);
    }

    @Override
    public AuthenticationStepContextFactory remove(String key) {
        return authenticationStepContextFactories.remove(key);
    }

    @Override
    public AuthenticationStepContextFactory get(Object key) {
        return authenticationStepContextFactories.get(key);
    }

    @Override
    public AuthenticationStepContextFactory getOrDefault(Object key, AuthenticationStepContextFactory def) {
        return authenticationStepContextFactories.getOrDefault(key, def);
    }

    @Override
    public boolean containsKey(Object key) {
        return authenticationStepContextFactories.containsKey(key);
    }

    public AuthenticationStepContext createContext(Account account) {
        List<String> stepNames = PLUGIN.getConfig().getAuthenticationSteps();
        String stepName = stepNames.get(0); // Use first stepName by default
        if (stepNames.size() > account.getCurrentConfigurationAuthenticationStepCreatorIndex()) // If current
            // authentication step
            // index not out of
            // bounds stepNames size
            stepName = stepNames.get(account.getCurrentConfigurationAuthenticationStepCreatorIndex()); // use this step
        // name
        return createContext(stepName, account);

    }

    public AuthenticationStepContext createContext(String stepName, Account account) {
        return authenticationStepContextFactories.getOrDefault(stepName, AuthenticationStepContextFactory.of(new DefaultAuthenticationStepContext(account))).createContext(account);
    }

}
