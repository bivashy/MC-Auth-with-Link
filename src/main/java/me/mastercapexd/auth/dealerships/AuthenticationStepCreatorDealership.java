package me.mastercapexd.auth.dealerships;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import me.mastercapexd.auth.authentication.step.creators.AuthenticationStepCreator;

public class AuthenticationStepCreatorDealership implements ListDealership<AuthenticationStepCreator> {

    private final List<AuthenticationStepCreator> authenticationSteps = new ArrayList<>();

    @Override
    public List<AuthenticationStepCreator> getList() {
        return Collections.unmodifiableList(authenticationSteps);
    }

    @Override
    public void add(AuthenticationStepCreator authenticationStepCreator) {
        if (authenticationStepCreator == null)
            return;
        if (authenticationSteps.contains(authenticationStepCreator))
            return;
        if (findFirstByPredicate(authStep -> authStep.getAuthenticationStepName().equals(authenticationStepCreator.getAuthenticationStepName())).orElse(null) != null)
            return;
        authenticationSteps.add(authenticationStepCreator);
    }

    @Override
    public void remove(AuthenticationStepCreator authenticationStepCreator) {
        if (authenticationStepCreator == null)
            return;
        authenticationSteps.remove(authenticationStepCreator);
    }

    @Override
    public Iterator<AuthenticationStepCreator> iterator() {
        return authenticationSteps.iterator();
    }

}
