package me.mastercapexd.auth.event;

import me.mastercapexd.auth.event.base.AccountEvent;
import me.mastercapexd.auth.event.base.CancellableEvent;

/**
 * Called when player have session and enters to the server. Cancel prevents skipping login process
 */
public interface AccountSessionEnterEvent extends AccountEvent, CancellableEvent {
}
