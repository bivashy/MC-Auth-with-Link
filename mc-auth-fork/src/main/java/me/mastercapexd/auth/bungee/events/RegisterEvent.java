package me.mastercapexd.auth.bungee.events;

import me.mastercapexd.auth.account.Account;

public class RegisterEvent extends AccountEvent implements Cancellable {
	private boolean isCancelled = false;

	public RegisterEvent(Account account) {
		super(account);
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
