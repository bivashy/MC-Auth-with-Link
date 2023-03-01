package com.bivashy.auth.api.step;

import com.bivashy.auth.api.account.Account;

public interface AuthenticationStepContext {
    Account getAccount();

    boolean canPassToNextStep();

    void setCanPassToNextStep(boolean canPass);
}
