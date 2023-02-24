package me.mastercapexd.auth.event;

import me.mastercapexd.auth.event.base.AccountEvent;
import me.mastercapexd.auth.event.base.CancellableEvent;

/**
 * Called when Account was <b>validated</b>. Cancel results skipping authentication like session, but without message (except BungeeCord).
 */
public interface AccountJoinEvent extends AccountEvent, CancellableEvent {
}
