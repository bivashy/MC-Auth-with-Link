package me.mastercapexd.auth.event.base;

import io.github.revxrsal.eventbus.gen.Index;

public interface PasswordCheckEvent {
    @Index(2)
    boolean isRightPassword();
}
