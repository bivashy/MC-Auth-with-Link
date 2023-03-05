package me.mastercapexd.auth.step.impl;

import com.bivashy.auth.api.step.AuthenticationStepContext;

import me.mastercapexd.auth.step.AuthenticationStepTemplate;
import me.mastercapexd.auth.step.creators.AuthenticationStepFactoryTemplate;

public class NullAuthenticationStep extends AuthenticationStepTemplate {
    private static final String STEP_NAME = "NULL";

    public NullAuthenticationStep() {
        super(STEP_NAME, null);
    }

    @Override
    public boolean shouldPassToNextStep() {
        return true;
    }

    @Override
    public boolean shouldSkip() {
        return true;
    }

    public static class NullAuthenticationStepFactory extends AuthenticationStepFactoryTemplate {
        public NullAuthenticationStepFactory() {
            super(STEP_NAME);
        }

        @Override
        public NullAuthenticationStep createNewAuthenticationStep(AuthenticationStepContext context) {
            return new NullAuthenticationStep();
        }
    }
}
