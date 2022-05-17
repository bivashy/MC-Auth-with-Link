package me.mastercapexd.auth.bungee.events;

import me.mastercapexd.auth.account.Account;

/** Deprecated LoginEvent. Use NewAuthenticationStepEvent for this purpose **/
@Deprecated
public class LoginEvent extends AccountEvent implements Cancellable {
	private boolean isForced = false;
	private boolean isCancelled = false;

	public LoginEvent(Account account) {
		super(account);
	}

	public LoginEvent(Account account, boolean isForced) {
		super(account);
		this.isForced = isForced;
	}

	public boolean isForced() {
		return isForced;
	}

	@Override
	public void setCancelled(boolean cancelValue) {
		this.isCancelled = cancelValue;
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

}
