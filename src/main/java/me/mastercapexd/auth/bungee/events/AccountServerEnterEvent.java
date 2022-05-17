package me.mastercapexd.auth.bungee.events;

import me.mastercapexd.auth.account.Account;

/**
 * Called when player enters to the game server (Session enter, on
 * login,register, on time left, force login etc.)
 */

public class AccountServerEnterEvent extends AccountEvent implements Cancellable {
	private final String id;
	private boolean isCancelled = false;

	public AccountServerEnterEvent(Account account, String id) {
		super(account);
		this.id = id;
	}

	public String getID() {
		return id;
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
