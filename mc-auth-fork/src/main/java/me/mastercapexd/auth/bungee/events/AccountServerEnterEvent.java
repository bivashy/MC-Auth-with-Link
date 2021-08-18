package me.mastercapexd.auth.bungee.events;

import me.mastercapexd.auth.Account;
import net.md_5.bungee.api.plugin.Event;

/**
 * Called when player enters to the game server (Session enter, on login,register, on time left, force login etc.)
 */

public class AccountServerEnterEvent extends Event implements Cancellable {
	private final Account account;
	private final String id;
	private boolean isCancelled = false;

	public AccountServerEnterEvent(Account account, String id) {
		this.account = account;
		this.id = id;
	}

	public Account getAccount() {
		return account;
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
