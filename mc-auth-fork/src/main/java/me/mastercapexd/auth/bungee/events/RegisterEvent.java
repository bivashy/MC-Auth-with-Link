package me.mastercapexd.auth.bungee.events;

import me.mastercapexd.auth.Account;
import net.md_5.bungee.api.plugin.Event;

public class RegisterEvent extends Event implements Cancellable{
	private final Account account;
	private boolean isCancelled = false;

	public RegisterEvent(Account account) {
		this.account = account;
	}

	public Account getAccount() {
		return account;
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
