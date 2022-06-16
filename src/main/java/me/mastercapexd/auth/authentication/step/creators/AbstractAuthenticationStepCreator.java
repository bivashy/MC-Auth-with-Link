package me.mastercapexd.auth.authentication.step.creators;

public abstract class AbstractAuthenticationStepCreator implements AuthenticationStepCreator {
    protected final String authenticationStepName;

    public AbstractAuthenticationStepCreator(String authenticationStepName) {
        this.authenticationStepName = authenticationStepName;
    }

    @Override
    public String getAuthenticationStepName() {
        return authenticationStepName;
    }

}
