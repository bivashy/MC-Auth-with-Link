package me.mastercapexd.auth.bungee.events;

import me.mastercapexd.auth.Account;
import me.mastercapexd.auth.VKEnterAnswer;
import net.md_5.bungee.api.plugin.Event;

/**
 * Called when player accepts or declines enter to acccount
 */
public class EntryConfirmationSelectEvent extends Event implements Cancellable{
	private final Integer userId;
	private final Account linkedAccount;
	private final VKEnterAnswer selected;
	private boolean isCancelled = false;

	public EntryConfirmationSelectEvent(Integer userId, Account linkedAccount,VKEnterAnswer selected) {
		this.userId = userId;
		this.linkedAccount = linkedAccount;
		this.selected = selected;
	}

	public Integer getUserId() {
		return userId;
	}

	public Account getLinkedAccount() {
		return linkedAccount;
	}

	public VKEnterAnswer getEnterSelected() {
		return selected;
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
