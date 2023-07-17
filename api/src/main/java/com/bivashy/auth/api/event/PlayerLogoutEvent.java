package com.bivashy.auth.api.event;

import com.bivashy.auth.api.event.base.CancellableEvent;
import com.bivashy.auth.api.event.base.PlayerEvent;

/**
 * Called when player or admin executes /logout. Cancel results preventing logging out
 */
public interface PlayerLogoutEvent extends PlayerEvent, CancellableEvent {
}
