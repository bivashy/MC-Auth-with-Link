package me.mastercapexd.auth.bucket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import com.bivashy.auth.api.bucket.AuthenticationStepFactoryBucket;
import com.bivashy.auth.api.factory.AuthenticationStepFactory;

public class BaseAuthenticationStepFactoryBucket implements AuthenticationStepFactoryBucket {
    private final List<AuthenticationStepFactory> authenticationSteps = new ArrayList<>();

    public List<AuthenticationStepFactory> getList() {
        return Collections.unmodifiableList(authenticationSteps);
    }

    public void add(AuthenticationStepFactory authenticationStepCreator) {
        if (authenticationStepCreator == null)
            return;
        if (authenticationSteps.contains(authenticationStepCreator))
            return;
        if (authenticationSteps.stream()
                .anyMatch(authStep -> authStep.getAuthenticationStepName().equals(authenticationStepCreator.getAuthenticationStepName())))
            return;
        authenticationSteps.add(authenticationStepCreator);
    }

    public void remove(AuthenticationStepFactory authenticationStepCreator) {
        if (authenticationStepCreator == null)
            return;
        authenticationSteps.remove(authenticationStepCreator);
    }

    public Iterator<AuthenticationStepFactory> iterator() {
        return authenticationSteps.iterator();
    }

    public Optional<AuthenticationStepFactory> findFirst(Predicate<AuthenticationStepFactory> filter) {
        return authenticationSteps.stream().filter(filter).findFirst();
    }
}
