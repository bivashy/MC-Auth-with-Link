package me.mastercapexd.auth.authentication.step.creators;

import me.mastercapexd.auth.authentication.step.AuthenticationStep;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;

public interface AuthenticationStepCreator {
	String getAuthenticationStepName();

	AuthenticationStep createNewAuthenticationStep(AuthenticationStepContext context);
}
