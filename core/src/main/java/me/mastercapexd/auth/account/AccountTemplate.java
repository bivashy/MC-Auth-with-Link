package me.mastercapexd.auth.account;

import io.github.revxrsal.eventbus.PostResult;
import me.mastercapexd.auth.authentication.step.AuthenticationStep;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.creators.AuthenticationStepCreator;
import me.mastercapexd.auth.authentication.step.steps.NullAuthenticationStep;
import me.mastercapexd.auth.authentication.step.steps.NullAuthenticationStep.NullAuthenticationStepCreator;
import me.mastercapexd.auth.event.AccountNewStepRequestEvent;
import me.mastercapexd.auth.event.AccountStepChangeEvent;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public abstract class AccountTemplate implements Account, Comparable<AccountTemplate> {
    private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();
    private Integer currentConfigurationAuthenticationStepCreatorIndex = 0;
    private AuthenticationStep currentAuthenticationStep = new NullAuthenticationStep();

    @Override
    public void nextAuthenticationStep(AuthenticationStepContext stepContext) {
        PLUGIN.getEventBus().publish(AccountNewStepRequestEvent.class, this, false, stepContext).thenAcceptAsync(stepRequestEventPostResult -> {
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
            AuthenticationStepCreator authenticationStepCreator = PLUGIN.getAuthenticationStepCreatorDealership()
                    .findFirst(stepCreator -> stepCreator.getAuthenticationStepName().equals(stepCreatorName))
                    .orElse(new NullAuthenticationStepCreator());
            AuthenticationStep newAuthenticationStep = authenticationStepCreator.createNewAuthenticationStep(stepContext);
            PostResult<AccountStepChangeEvent> stepChangeEventPostResult = PLUGIN.getEventBus()
                    .publish(AccountStepChangeEvent.class, this, false, stepContext, currentAuthenticationStep, newAuthenticationStep)
                    .join();
            if (stepChangeEventPostResult.getEvent().isCancelled())
                return;
            currentAuthenticationStep = newAuthenticationStep;
            currentConfigurationAuthenticationStepCreatorIndex += 1;
            if (currentAuthenticationStep.shouldSkip()) {
                currentAuthenticationStep = new NullAuthenticationStep();
                nextAuthenticationStep(PLUGIN.getAuthenticationContextFactoryDealership().createContext(this));
            }
        });
    }

    @Override
    public int getCurrentAuthenticationStepCreatorIndex() {
        return currentConfigurationAuthenticationStepCreatorIndex;
    }

    @Override
    public void setCurrentAuthenticationStepCreatorIndex(int index) {
        String stepName = PLUGIN.getConfig().getAuthenticationStepName(index);

        AuthenticationStepCreator authenticationStepCreator = PLUGIN.getAuthenticationStepCreatorDealership()
                .findFirst(stepCreator -> stepCreator.getAuthenticationStepName().equals(stepName))
                .orElse(new NullAuthenticationStepCreator());

        AuthenticationStepContext stepContext = PLUGIN.getAuthenticationContextFactoryDealership().createContext(stepName, this);
        currentConfigurationAuthenticationStepCreatorIndex = index;
        currentAuthenticationStep = authenticationStepCreator.createNewAuthenticationStep(stepContext);
    }

    @Override
    public AuthenticationStep getCurrentAuthenticationStep() {
        if (currentAuthenticationStep.shouldPassToNextStep())
            nextAuthenticationStep(PLUGIN.getAuthenticationContextFactoryDealership().createContext(this));

        return currentAuthenticationStep;
    }
}