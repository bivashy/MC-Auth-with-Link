package com.bivashy.auth.api.bucket;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import com.bivashy.auth.api.factory.AuthenticationStepFactory;

public interface AuthenticationStepFactoryBucket {
    List<AuthenticationStepFactory> getList();

    void add(AuthenticationStepFactory authenticationStepCreator);

    void remove(AuthenticationStepFactory authenticationStepCreator);

    Iterator<AuthenticationStepFactory> iterator();

    Optional<AuthenticationStepFactory> findFirst(Predicate<AuthenticationStepFactory> filter);
}
