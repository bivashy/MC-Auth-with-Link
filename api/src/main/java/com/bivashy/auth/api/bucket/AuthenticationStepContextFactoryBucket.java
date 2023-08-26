package com.bivashy.auth.api.bucket;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.bucket.AuthenticationStepContextFactoryBucket.AuthenticationStepContextFactoryWrapper;
import com.bivashy.auth.api.factory.AuthenticationStepContextFactory;
import com.bivashy.auth.api.step.AuthenticationStepContext;

public interface AuthenticationStepContextFactoryBucket extends Bucket<AuthenticationStepContextFactoryWrapper> {

    @Deprecated
    default Map<String, AuthenticationStepContextFactory> getMap() {
        return stream().collect(Collectors.toMap(AuthenticationStepContextFactoryWrapper::getIdentifier, Function.identity()));
    }

    @Deprecated
    default AuthenticationStepContextFactory put(String key, AuthenticationStepContextFactory value) {
        modifiable().add(AuthenticationStepContextFactoryWrapper.of(key, value));
        return value;
    }

    @Deprecated
    default AuthenticationStepContextFactory remove(String key) {
        Optional<AuthenticationStepContextFactoryWrapper> factoryOptional = findFirstByValue(AuthenticationStepContextFactoryWrapper::getIdentifier, key);
        factoryOptional.ifPresent(factory -> modifiable().removeIf(wrapper -> factory.getIdentifier().equals(wrapper.getIdentifier())));
        return factoryOptional.orElse(null);
    }

    @Deprecated
    default AuthenticationStepContextFactory get(String key) {
        return findFirstByValue(AuthenticationStepContextFactoryWrapper::getIdentifier, key).orElse(null);
    }

    @Deprecated
    default AuthenticationStepContextFactory getOrDefault(String key, AuthenticationStepContextFactory def) {
        return findFirstByValue(AuthenticationStepContextFactoryWrapper::getIdentifier, key).map(wrapper -> (AuthenticationStepContextFactory) wrapper).orElse(def);
    }

    @Deprecated
    default boolean containsKey(String key) {
        return hasByValue(AuthenticationStepContextFactoryWrapper::getIdentifier, key);
    }

    AuthenticationStepContext createContext(Account account);

    AuthenticationStepContext createContext(String stepName, Account account);

    interface AuthenticationStepContextFactoryWrapper extends AuthenticationStepContextFactory {

        static AuthenticationStepContextFactoryWrapper of(String identifier, AuthenticationStepContextFactory factory) {
            return new AuthenticationStepContextFactoryWrapper() {
                @Override
                public String getIdentifier() {
                    return identifier;
                }

                @Override
                public AuthenticationStepContext createContext(Account account) {
                    return factory.createContext(account);
                }
            };
        }

        String getIdentifier();

    }

}
