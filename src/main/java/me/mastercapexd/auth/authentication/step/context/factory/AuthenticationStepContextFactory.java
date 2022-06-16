package me.mastercapexd.auth.authentication.step.context.factory;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;

import java.util.function.Supplier;

public interface AuthenticationStepContextFactory {
    static AuthenticationStepContextFactory of(Supplier<AuthenticationStepContext> supplier) {
        return of(supplier.get());
    }

    static AuthenticationStepContextFactory of(AuthenticationStepContext context) {
        return (account) -> context;
    }

    AuthenticationStepContext createContext(Account account);
}
