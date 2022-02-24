package me.mastercapexd.auth.dealerships;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.context.DefaultAuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.context.factory.AuthenticationStepContextFactory;

public class AuthenticationStepContextFactoryDealership
		implements MapDealership<String, AuthenticationStepContextFactory> {
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

	public AuthenticationStepContext createContext(String stepName, Account account) {
		return authenticationStepContextFactories
				.getOrDefault(stepName,
						AuthenticationStepContextFactory.of(new DefaultAuthenticationStepContext(account))).createContext(account);
	}

}
