package me.mastercapexd.auth.authentication.step.context;

import me.mastercapexd.auth.account.Account;

public interface AuthenticationStepContext {
	Account getAccount();
	
	boolean canPassToNextStep();
	
	void setCanPassToNextStep(boolean canPass);
}
