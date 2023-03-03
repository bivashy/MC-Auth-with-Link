package com.bivashy.auth.api.event.base;

import com.bivashy.auth.api.account.Account;

import io.github.revxrsal.eventbus.gen.Index;

public interface AccountEvent {
    @Index(0)
    Account getAccount();
}
