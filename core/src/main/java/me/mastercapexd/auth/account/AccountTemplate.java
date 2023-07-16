package me.mastercapexd.auth.account;

import java.util.concurrent.CompletableFuture;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.event.AccountNewStepRequestEvent;
import com.bivashy.auth.api.event.AccountStepChangeEvent;
import com.bivashy.auth.api.factory.AuthenticationStepFactory;
import com.bivashy.auth.api.step.AuthenticationStep;
import com.bivashy.auth.api.step.AuthenticationStepContext;

import io.github.revxrsal.eventbus.PostResult;
import me.mastercapexd.auth.step.impl.NullAuthenticationStep;
import me.mastercapexd.auth.step.impl.NullAuthenticationStep.NullAuthenticationStepFactory;

public abstract class AccountTemplate implements Account, Comparable<AccountTemplate> {
    private static final AuthPlugin PLUGIN = AuthPlugin.instance();
    private Integer currentConfigurationAuthenticationStepCreatorIndex = 0;
    private AuthenticationStep currentAuthenticationStep = new NullAuthenticationStep();

    @Override
    public CompletableFuture<Void> nextAuthenticationStep(AuthenticationStepContext stepContext) {
        return PLUGIN.getEventBus().publish(AccountNewStepRequestEvent.class, this, false, stepContext).thenAcceptAsync(stepRequestEventPostResult -> {
            if (stepRequestEventPostResult.getEvent().isCancelled())
                return;
            if (stepContext == null)
                return;
            if (currentAuthenticationStep != null && !currentAuthenticationStep.shouldPassToNextStep())
                return;
            if (PLUGIN.getConfig().getAuthenticationSteps().size() <= currentConfigurationAuthenticationStepCreatorIndex) {
                currentConfigurationAuthenticationStepCreatorIndex = 0;
                return;
            }
            String stepCreatorName = PLUGIN.getConfig().getAuthenticationStepName(currentConfigurationAuthenticationStepCreatorIndex);
            AuthenticationStepFactory authenticationStepFactory = PLUGIN.getAuthenticationStepFactoryBucket()
                    .findFirst(stepCreator -> stepCreator.getAuthenticationStepName().equals(stepCreatorName))
                    .orElse(new NullAuthenticationStepFactory());
            AuthenticationStep newAuthenticationStep = authenticationStepFactory.createNewAuthenticationStep(stepContext);
            PostResult<AccountStepChangeEvent> stepChangeEventPostResult = PLUGIN.getEventBus()
                    .publish(AccountStepChangeEvent.class, this, false, stepContext, currentAuthenticationStep, newAuthenticationStep)
                    .join();
            if (stepChangeEventPostResult.getEvent().isCancelled())
                return;
            currentAuthenticationStep = newAuthenticationStep;
            currentConfigurationAuthenticationStepCreatorIndex += 1;
            if (currentAuthenticationStep.shouldSkip()) {
                currentAuthenticationStep = new NullAuthenticationStep();
                nextAuthenticationStep(PLUGIN.getAuthenticationContextFactoryBucket().createContext(this)).join();
            }
        });
    }

    @Override
    public boolean isSessionActive(long sessionDurability) {
        long sessionEndTime = getLastSessionStartTimestamp() + sessionDurability;
        return sessionEndTime >= System.currentTimeMillis() && !PLUGIN.getAuthenticatingAccountBucket().isAuthenticating(this);
    }

    @Override
    public int getCurrentAuthenticationStepCreatorIndex() {
        return currentConfigurationAuthenticationStepCreatorIndex;
    }

    @Override
    public void setCurrentAuthenticationStepCreatorIndex(int index) {
        String stepName = PLUGIN.getConfig().getAuthenticationStepName(index);

        AuthenticationStepFactory authenticationStepFactory = PLUGIN.getAuthenticationStepFactoryBucket()
                .findFirst(stepCreator -> stepCreator.getAuthenticationStepName().equals(stepName))
                .orElse(new NullAuthenticationStepFactory());

        AuthenticationStepContext stepContext = PLUGIN.getAuthenticationContextFactoryBucket().createContext(stepName, this);
        currentConfigurationAuthenticationStepCreatorIndex = index;
        currentAuthenticationStep = authenticationStepFactory.createNewAuthenticationStep(stepContext);
    }

    @Override
    public AuthenticationStep getCurrentAuthenticationStep() {
        if (currentAuthenticationStep.shouldPassToNextStep())
            nextAuthenticationStep(PLUGIN.getAuthenticationContextFactoryBucket().createContext(this));

        return currentAuthenticationStep;
    }
}