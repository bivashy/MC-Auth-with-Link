package me.mastercapexd.auth.step;

import com.bivashy.auth.api.step.AuthenticationStep;
import com.bivashy.auth.api.step.AuthenticationStepContext;

public abstract class AuthenticationStepTemplate implements AuthenticationStep {
    protected final String stepName;
    protected final AuthenticationStepContext authenticationStepContext;

    public AuthenticationStepTemplate(String stepName, AuthenticationStepContext authenticationStepContext) {
        this.stepName = stepName;
        this.authenticationStepContext = authenticationStepContext;
    }

    @Override
    public String getStepName() {
        return stepName;
    }

    @Override
    public AuthenticationStepContext getAuthenticationStepContext() {
        return authenticationStepContext;
    }
}
