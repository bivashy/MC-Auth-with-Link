package me.mastercapexd.auth.event;

import me.mastercapexd.auth.event.base.AccountEvent;
import me.mastercapexd.auth.event.base.CancellableEvent;
import me.mastercapexd.auth.event.base.PasswordCheckEvent;

/**
 * Called when player tried to enter password with <b>command</b>. Cancel results for do not validating, but do not prevent "wrong attempt" increasing.
 */
public interface AccountTryLoginEvent extends AccountEvent, CancellableEvent, PasswordCheckEvent {
}
