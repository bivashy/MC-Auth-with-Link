package me.mastercapexd.auth.bucket;

import java.util.List;
import java.util.Optional;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.bucket.AuthenticationStepContextFactoryBucket;
import com.bivashy.auth.api.bucket.AuthenticationStepContextFactoryBucket.AuthenticationStepContextFactoryWrapper;
import com.bivashy.auth.api.factory.AuthenticationStepContextFactory;
import com.bivashy.auth.api.step.AuthenticationStepContext;

import me.mastercapexd.auth.step.context.BaseAuthenticationStepContext;

public class BaseAuthenticationStepContextFactoryBucket extends BaseListBucket<AuthenticationStepContextFactoryWrapper> implements AuthenticationStepContextFactoryBucket {

    private final List<String> stepNames;

    public BaseAuthenticationStepContextFactoryBucket(List<String> stepNames) {
        this.stepNames = stepNames;
    }

    public AuthenticationStepContext createContext(Account account) {
        String stepName = stepNames.get(0);
        if (stepNames.size() > account.getCurrentAuthenticationStepCreatorIndex())
            stepName = stepNames.get(account.getCurrentAuthenticationStepCreatorIndex());
        return createContext(stepName, account);
    }

    public AuthenticationStepContext createContext(String stepName, Account account) {
        Optional<AuthenticationStepContextFactoryWrapper> wrapperOptional = findFirstByValue(AuthenticationStepContextFactoryWrapper::getIdentifier, stepName);
        AuthenticationStepContextFactory defaultFactory = AuthenticationStepContextFactory.of(new BaseAuthenticationStepContext(account));
        return wrapperOptional.orElse(AuthenticationStepContextFactoryWrapper.of(stepName, defaultFactory)).createContext(account);
    }

}
