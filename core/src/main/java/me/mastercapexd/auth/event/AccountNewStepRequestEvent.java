package me.mastercapexd.auth.event;

import io.github.revxrsal.eventbus.gen.Index;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.event.base.AccountEvent;
import me.mastercapexd.auth.event.base.CancellableEvent;

/**
 * Called when {@link me.mastercapexd.auth.account.Account#nextAuthenticationStep(AuthenticationStepContext)} <b>called</b>. Cancel results for just ignoring
 * AuthenticationStep
 */
public interface AccountNewStepRequestEvent extends AccountEvent, CancellableEvent {
    @Index(2)
    AuthenticationStepContext getContext();
}
