package me.mastercapexd.auth.account;

import me.mastercapexd.auth.authentication.step.AuthenticationStep;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.creators.AuthenticationStepCreator;
import me.mastercapexd.auth.authentication.step.steps.NullAuthenticationStep;
import me.mastercapexd.auth.authentication.step.steps.NullAuthenticationStep.NullAuthenticationStepCreator;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public abstract class AbstractAccount implements Account, Comparable<AbstractAccount> {

    private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();

    private Integer currentConfigurationAuthenticationStepCreatorIndex = 0;

    private AuthenticationStep currentAuthenticationStep = new NullAuthenticationStep();

    @Override
    public boolean nextAuthenticationStep(AuthenticationStepContext stepContext) {
        if (stepContext == null)
            return false;
        if (currentAuthenticationStep != null && !currentAuthenticationStep.shouldPassToNextStep())
            return false;
        if (PLUGIN.getConfig().getAuthenticationSteps().size() <= currentConfigurationAuthenticationStepCreatorIndex) {
            currentConfigurationAuthenticationStepCreatorIndex = 0;
            return false;
        }
        String stepCreatorName = PLUGIN.getConfig().getAuthenticationStepName(currentConfigurationAuthenticationStepCreatorIndex);
        AuthenticationStepCreator authenticationStepCreator =
                PLUGIN.getAuthenticationStepCreatorDealership().findFirstByPredicate(stepCreator -> stepCreator.getAuthenticationStepName().equals(stepCreatorName)).orElse(new NullAuthenticationStepCreator());
        currentAuthenticationStep = authenticationStepCreator.createNewAuthenticationStep(stepContext);
        currentConfigurationAuthenticationStepCreatorIndex += 1;
        if (currentAuthenticationStep.shouldSkip()) {
            currentAuthenticationStep = new NullAuthenticationStep();
            return nextAuthenticationStep(PLUGIN.getAuthenticationContextFactoryDealership().createContext(this));
        }
        return true;
    }

    @Override
    public int getCurrentConfigurationAuthenticationStepCreatorIndex() {
        return currentConfigurationAuthenticationStepCreatorIndex;
    }

    @Override
    public void setCurrentConfigurationAuthenticationStepCreatorIndex(int index) {
        String stepName = PLUGIN.getConfig().getAuthenticationStepName(index);

        AuthenticationStepCreator authenticationStepCreator =
                PLUGIN.getAuthenticationStepCreatorDealership().findFirstByPredicate(stepCreator -> stepCreator.getAuthenticationStepName().equals(stepName)).orElse(new NullAuthenticationStepCreator());

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