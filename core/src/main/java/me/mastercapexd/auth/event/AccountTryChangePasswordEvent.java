package me.mastercapexd.auth.event;

import me.mastercapexd.auth.event.base.AccountEvent;
import me.mastercapexd.auth.event.base.CancellableEvent;
import me.mastercapexd.auth.event.base.PasswordCheckEvent;

/**
 * Called when player changes tries to change his password. Cancel prevents validating, and sending messages.
 */
public interface AccountTryChangePasswordEvent extends AccountEvent, CancellableEvent, PasswordCheckEvent {
}
