package com.bivashy.auth.api.event;

import com.bivashy.auth.api.event.base.AccountEvent;
import com.bivashy.auth.api.event.base.CancellableEvent;

/**
 * Called when player registers. Cancel prevents saving data to the database and logging in player.
 */
public interface AccountRegisterEvent extends AccountEvent, CancellableEvent {
}
