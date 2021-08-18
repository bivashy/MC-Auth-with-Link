package me.mastercapexd.auth.bungee.events;

public interface Cancellable {
	public void setCancelled(boolean cancelValue);

	public boolean isCancelled();
}
