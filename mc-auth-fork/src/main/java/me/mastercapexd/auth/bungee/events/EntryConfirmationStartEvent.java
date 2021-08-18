package me.mastercapexd.auth.bungee.events;

import me.mastercapexd.auth.Account;
import net.md_5.bungee.api.plugin.Event;

public class EntryConfirmationStartEvent extends Event{
	private final Integer userId;
	private final Account linkedAccount;
	private boolean isCancelled = false;

	public EntryConfirmationStartEvent(Integer userId, Account linkedAccount) {
		this.userId = userId;
		this.linkedAccount = linkedAccount;
	}

	public Integer getUserId() {
		return userId;
	}

	public Account getLinkedAccount() {
		return linkedAccount;
	}
}
