package com.bivashy.auth.api.event;

import com.bivashy.auth.api.event.base.AccountEvent;
import com.bivashy.auth.api.event.base.CancellableEvent;
import com.bivashy.auth.api.event.base.LinkEvent;
import com.bivashy.auth.api.shared.commands.MessageableCommandActor;

import io.github.revxrsal.eventbus.gen.Index;

/**
 * Called when player executes /entertoggle. Cancel prevents toggling.
 */
public interface AccountLinkConfirmationToggleEvent extends AccountEvent, CancellableEvent, LinkEvent {
    @Index(4)
    MessageableCommandActor getActor();
}
