package com.bivashy.auth.api.event.base;

import java.util.Optional;

import com.bivashy.auth.api.account.Account;

import io.github.revxrsal.eventbus.gen.Index;

public interface OptionalAccountEvent {
    @Index(0)
    Optional<Account> getAccount();
}
