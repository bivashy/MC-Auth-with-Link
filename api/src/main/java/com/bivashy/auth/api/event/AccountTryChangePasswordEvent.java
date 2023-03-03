package com.bivashy.auth.api.event;

import com.bivashy.auth.api.event.base.AccountEvent;
import com.bivashy.auth.api.event.base.CancellableEvent;
import com.bivashy.auth.api.event.base.PasswordCheckEvent;

/**
 * Called when player changes tries to change his password. Cancel prevents validating, and sending messages.
 */
public interface AccountTryChangePasswordEvent extends AccountEvent, CancellableEvent, PasswordCheckEvent {
}
