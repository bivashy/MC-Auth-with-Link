package me.mastercapexd.auth.bungee.events;

import me.mastercapexd.auth.VKEnterAnswer;
import me.mastercapexd.auth.account.Account;

/**
 * Called when player accepts or declines enter to acccount
 */
public class EntryConfirmationSelectEvent extends AccountEvent implements Cancellable {
	private final Integer userId;
	private final VKEnterAnswer selected;
	private boolean isCancelled = false;

	public EntryConfirmationSelectEvent(Integer userId, Account linkedAccount, VKEnterAnswer selected) {
		super(linkedAccount);
		this.userId = userId;
		this.selected = selected;
	}

	public Integer getUserId() {
		return userId;
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
