package me.mastercapexd.auth.bungee.events;

public interface Cancellable {
	void setCancelled(boolean cancelValue);

	boolean isCancelled();
}
