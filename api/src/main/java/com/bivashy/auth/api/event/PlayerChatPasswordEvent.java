package com.bivashy.auth.api.event;

import com.bivashy.auth.api.event.base.PlayerEvent;
import io.github.revxrsal.eventbus.gen.Index;

public interface PlayerChatPasswordEvent extends PlayerEvent {
    @Index(1)
    String getPassword();
}
