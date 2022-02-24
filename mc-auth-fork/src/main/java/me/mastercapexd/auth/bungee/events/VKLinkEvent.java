package me.mastercapexd.auth.bungee.events;

import me.mastercapexd.auth.account.Account;

public class VKLinkEvent extends AccountEvent implements Cancellable {
	private final Integer userId;
	private boolean isCancelled = false;

	public VKLinkEvent(Integer userId, Account linkedAccount) {
		super(linkedAccount);
		this.userId = userId;
	}

	public Integer getUserId() {
		return userId;
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
