package me.mastercapexd.auth.event;

import me.mastercapexd.auth.event.base.AccountEvent;
import me.mastercapexd.auth.event.base.CancellableEvent;

/**
 * Called when player registers. Cancel prevents saving data to the database and logging in player.
 */
public interface AccountRegisterEvent extends AccountEvent, CancellableEvent {
}
