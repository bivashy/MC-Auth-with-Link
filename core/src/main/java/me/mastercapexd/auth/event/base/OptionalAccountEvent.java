package me.mastercapexd.auth.event.base;

import java.util.Optional;

import io.github.revxrsal.eventbus.gen.Index;
import me.mastercapexd.auth.account.Account;

public interface OptionalAccountEvent {
    @Index(0)
    Optional<Account> getAccount();
}
