package me.mastercapexd.auth.step.creators;

import com.bivashy.auth.api.factory.AuthenticationStepFactory;

public abstract class AuthenticationStepFactoryTemplate implements AuthenticationStepFactory {
    protected final String authenticationStepName;

    public AuthenticationStepFactoryTemplate(String authenticationStepName) {
        this.authenticationStepName = authenticationStepName;
    }

    @Override
    public String getAuthenticationStepName() {
        return authenticationStepName;
    }
}
