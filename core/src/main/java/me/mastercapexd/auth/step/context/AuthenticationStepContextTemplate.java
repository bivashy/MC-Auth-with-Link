package me.mastercapexd.auth.step.context;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.step.AuthenticationStepContext;

public abstract class AuthenticationStepContextTemplate implements AuthenticationStepContext {
    protected final Account account;
    protected boolean canPassToNextStep = false;

    public AuthenticationStepContextTemplate(Account account) {
        this.account = account;
    }

    @Override
    public Account getAccount() {
        return account;
    }

    @Override
    public boolean canPassToNextStep() {
        return canPassToNextStep;
    }

    @Override
    public void setCanPassToNextStep(boolean canPass) {
        this.canPassToNextStep = canPass;
    }
}
