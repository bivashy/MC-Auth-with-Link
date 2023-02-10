package me.mastercapexd.auth.dealerships;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import me.mastercapexd.auth.authentication.step.creators.AuthenticationStepCreator;

public class AuthenticationStepCreatorDealership {
    private final List<AuthenticationStepCreator> authenticationSteps = new ArrayList<>();

    public List<AuthenticationStepCreator> getList() {
        return Collections.unmodifiableList(authenticationSteps);
    }

    public void add(AuthenticationStepCreator authenticationStepCreator) {
        if (authenticationStepCreator == null)
            return;
        if (authenticationSteps.contains(authenticationStepCreator))
            return;
        if (authenticationSteps.stream()
                .anyMatch(authStep -> authStep.getAuthenticationStepName().equals(authenticationStepCreator.getAuthenticationStepName())))
            return;
        authenticationSteps.add(authenticationStepCreator);
    }

    public void remove(AuthenticationStepCreator authenticationStepCreator) {
        if (authenticationStepCreator == null)
            return;
        authenticationSteps.remove(authenticationStepCreator);
    }

    public Iterator<AuthenticationStepCreator> iterator() {
        return authenticationSteps.iterator();
    }
}
