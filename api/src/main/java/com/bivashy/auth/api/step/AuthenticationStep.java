package com.bivashy.auth.api.step;

/**
 * @author Ubivashka Authentication step that control
 */
public interface AuthenticationStep {
    /**
     * @return step name. For example LOGIN, REGISTER, ENTER_SERVER. Used for configuration
     */
    String getStepName();

    /**
     * @return get context of step
     */
    AuthenticationStepContext getAuthenticationStepContext();

    /**
     * @return Should player pass to next authentication step
     */
    boolean shouldPassToNextStep();

    /**
     * @return Should process step, or skip this authentication step
     */
    boolean shouldSkip();
}
