package me.mastercapexd.auth.bungee.events;

import me.mastercapexd.auth.Account;
import net.md_5.bungee.api.plugin.Event;

public class LoginEvent extends Event implements Cancellable {
	private final Account account;
	private boolean isForced = false;
	private boolean isCancelled = false;

	public LoginEvent(Account account) {
		this.account = account;
	}

	public LoginEvent(Account account, boolean isForced) {
		this.account = account;
		this.isForced = isForced;
	}

	public Account getAccount() {
		return account;
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
