package me.mastercapexd.auth.authentication.step.context;

import me.mastercapexd.auth.account.Account;

public abstract class AbstractAuthenticationStepContext implements AuthenticationStepContext {
	protected final Account account;

	protected boolean canPassToNextStep = false;

	public AbstractAuthenticationStepContext(Account account) {
		this.account = account;
	}

	@Override
	public Account getAccount() {
		return account;
	}

	@Override
	public boolean canPassToNextStep() {
		return canPassToNextStep;
	}

	@Override
	public void setCanPassToNextStep(boolean canPass) {
		this.canPassToNextStep = canPass;
	}

}
