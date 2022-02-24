package me.mastercapexd.auth.authentication.step.context.factory;

import java.util.function.Supplier;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;

public interface AuthenticationStepContextFactory {
	AuthenticationStepContext createContext(Account account);

	static AuthenticationStepContextFactory of(Supplier<AuthenticationStepContext> supplier) {
		return of(supplier.get());
	}
	
	static AuthenticationStepContextFactory of(AuthenticationStepContext context) {
		return (account) -> context;
	}
}
