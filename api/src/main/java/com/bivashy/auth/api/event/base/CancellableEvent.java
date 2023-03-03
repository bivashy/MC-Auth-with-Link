package com.bivashy.auth.api.event.base;

import io.github.revxrsal.eventbus.gen.Index;

public interface CancellableEvent {
    @Index(1)
    boolean isCancelled();

    @Index(1)
    void setCancelled(boolean cancelled);
}
