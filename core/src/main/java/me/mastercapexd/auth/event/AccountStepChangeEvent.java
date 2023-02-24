package me.mastercapexd.auth.event;

import io.github.revxrsal.eventbus.gen.Index;
import me.mastercapexd.auth.authentication.step.AuthenticationStep;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.event.base.AccountEvent;
import me.mastercapexd.auth.event.base.CancellableEvent;

/**
 * Called when {@link me.mastercapexd.auth.account.Account#nextAuthenticationStep(AuthenticationStepContext)} validates and changes it`s current
 * {@link me.mastercapexd.auth.authentication.step.AuthenticationStep}. Cancel prevents settings current
 * {@link me.mastercapexd.auth.authentication.step.AuthenticationStep}
 */
public interface AccountStepChangeEvent extends AccountEvent, CancellableEvent {
    @Index(2)
    AuthenticationStepContext getCurrentContext();
    @Index(3)
    AuthenticationStep getOldStep();
    @Index(4)
    AuthenticationStep getNewStep();
}
