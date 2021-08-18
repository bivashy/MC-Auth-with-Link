package me.mastercapexd.auth.bungee.events;

import me.mastercapexd.auth.Account;
import net.md_5.bungee.api.plugin.Event;

public class VKUnlinkEvent extends Event implements Cancellable {
	private final Integer userId;
	private final Account unlinkedAccount;
	private boolean isCancelled = false;

	public VKUnlinkEvent(Integer userId, Account unlinkedAccount) {
		this.userId = userId;
		this.unlinkedAccount = unlinkedAccount;
	}

	public Integer getUserId() {
		return userId;
	}

	public Account getUnLinkedAccount() {
		return unlinkedAccount;
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
