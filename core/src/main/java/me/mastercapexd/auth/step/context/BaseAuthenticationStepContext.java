package me.mastercapexd.auth.step.context;

import com.bivashy.auth.api.account.Account;

public class BaseAuthenticationStepContext extends AuthenticationStepContextTemplate {
    public BaseAuthenticationStepContext(Account account) {
        super(account);
    }
}
