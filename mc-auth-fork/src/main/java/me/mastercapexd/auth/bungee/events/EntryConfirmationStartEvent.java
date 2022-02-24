package me.mastercapexd.auth.bungee.events;

import me.mastercapexd.auth.account.Account;

public class EntryConfirmationStartEvent extends AccountEvent {
	private final Integer userId;

	public EntryConfirmationStartEvent(Integer userId, Account linkedAccount) {
		super(linkedAccount);
		this.userId = userId;
	}

	public Integer getUserId() {
		return userId;
	}

}
