package me.mastercapexd.auth.authentication.step;

import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;

/**
 * @author Ubivashka Authentication step that control
 *
 */
public interface AuthenticationStep {

	/**
	 * @return step name. For example login,register,vklink, or something
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
