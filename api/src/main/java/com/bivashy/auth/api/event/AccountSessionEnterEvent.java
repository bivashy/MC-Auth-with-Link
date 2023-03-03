package com.bivashy.auth.api.event;

import com.bivashy.auth.api.event.base.AccountEvent;
import com.bivashy.auth.api.event.base.CancellableEvent;

/**
 * Called when player have session and enters to the server. Cancel prevents skipping login process
 */
public interface AccountSessionEnterEvent extends AccountEvent, CancellableEvent {
}
