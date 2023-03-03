package com.bivashy.auth.api.event;

import com.bivashy.auth.api.event.base.AccountEvent;
import com.bivashy.auth.api.event.base.CancellableEvent;
import com.bivashy.auth.api.event.base.PasswordCheckEvent;

/**
 * Called when player tried to enter password with <b>command</b>. Cancel results for do not validating, but do not prevent "wrong attempt" increasing.
 */
public interface AccountTryLoginEvent extends AccountEvent, CancellableEvent, PasswordCheckEvent {
}
