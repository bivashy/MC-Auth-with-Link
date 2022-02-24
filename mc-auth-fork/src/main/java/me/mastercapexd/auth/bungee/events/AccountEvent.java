package me.mastercapexd.auth.bungee.events;

import me.mastercapexd.auth.account.Account;
import net.md_5.bungee.api.plugin.Event;

public abstract class AccountEvent extends Event {
	private final Account account;

	public AccountEvent(Account account) {
		this.account = account;
	}

	public Account getAccount() {
		return account;
	}
}
