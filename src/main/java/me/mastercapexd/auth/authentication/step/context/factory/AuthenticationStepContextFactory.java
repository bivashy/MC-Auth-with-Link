package me.mastercapexd.auth.authentication.step.context.factory;

import java.util.function.Supplier;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;

public interface AuthenticationStepContextFactory {
    static AuthenticationStepContextFactory of(Supplier<AuthenticationStepContext> supplier) {
        return of(supplier.get());
    }

    static AuthenticationStepContextFactory of(AuthenticationStepContext context) {
        return (account) -> context;
    }

    AuthenticationStepContext createContext(Account account);
}
