package me.mastercapexd.auth.event.base;

import io.github.revxrsal.eventbus.gen.Index;
import me.mastercapexd.auth.account.Account;

public interface AccountEvent {
    @Index(0)
    Account getAccount();
}
