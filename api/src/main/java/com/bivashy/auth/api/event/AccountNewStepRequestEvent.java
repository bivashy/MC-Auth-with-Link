package com.bivashy.auth.api.event;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.event.base.AccountEvent;
import com.bivashy.auth.api.event.base.CancellableEvent;
import com.bivashy.auth.api.step.AuthenticationStepContext;

import io.github.revxrsal.eventbus.gen.Index;

/**
 * Called when {@link Account#nextAuthenticationStep(AuthenticationStepContext)} <b>called</b>. Cancel results for just ignoring
 * AuthenticationStep
 */
public interface AccountNewStepRequestEvent extends AccountEvent, CancellableEvent {
    @Index(2)
    AuthenticationStepContext getContext();
}
