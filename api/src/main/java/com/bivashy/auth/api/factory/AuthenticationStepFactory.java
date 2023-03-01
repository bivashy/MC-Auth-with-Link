package com.bivashy.auth.api.factory;

import com.bivashy.auth.api.step.AuthenticationStep;
import com.bivashy.auth.api.step.AuthenticationStepContext;

public interface AuthenticationStepFactory {
    String getAuthenticationStepName();

    AuthenticationStep createNewAuthenticationStep(AuthenticationStepContext context);
}
