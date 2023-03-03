package com.bivashy.auth.api.event;

import com.bivashy.auth.api.event.base.AccountEvent;
import com.bivashy.auth.api.event.base.CancellableEvent;

/**
 * Called when Account was <b>validated</b>. Cancel results skipping authentication like session, but without message (except BungeeCord).
 */
public interface AccountJoinEvent extends AccountEvent, CancellableEvent {
}
