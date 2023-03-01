package com.bivashy.auth.api.factory;

import java.util.function.Supplier;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.step.AuthenticationStepContext;

public interface AuthenticationStepContextFactory {
    static AuthenticationStepContextFactory of(Supplier<AuthenticationStepContext> supplier) {
        return of(supplier.get());
    }

    static AuthenticationStepContextFactory of(AuthenticationStepContext context) {
        return (account) -> context;
    }

    AuthenticationStepContext createContext(Account account);
}
