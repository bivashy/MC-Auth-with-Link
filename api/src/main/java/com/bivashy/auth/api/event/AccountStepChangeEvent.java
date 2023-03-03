package com.bivashy.auth.api.event;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.event.base.AccountEvent;
import com.bivashy.auth.api.event.base.CancellableEvent;
import com.bivashy.auth.api.step.AuthenticationStep;
import com.bivashy.auth.api.step.AuthenticationStepContext;

import io.github.revxrsal.eventbus.gen.Index;

/**
 * Called when {@link Account#nextAuthenticationStep(AuthenticationStepContext)} validates and changes it`s current
 * {@link AuthenticationStep}. Cancel prevents settings current
 * {@link AuthenticationStep}
 */
public interface AccountStepChangeEvent extends AccountEvent, CancellableEvent {
    @Index(2)
    AuthenticationStepContext getCurrentContext();
    @Index(3)
    AuthenticationStep getOldStep();
    @Index(4)
    AuthenticationStep getNewStep();
}
