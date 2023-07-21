package com.bivashy.auth.api.event;

import com.bivashy.auth.api.event.base.AccountEvent;
import com.bivashy.auth.api.event.base.CancellableEvent;
import com.bivashy.auth.api.event.base.LinkEvent;
import com.bivashy.auth.api.link.user.entry.LinkEntryUser;
import com.bivashy.auth.api.shared.commands.MessageableCommandActor;

import io.github.revxrsal.eventbus.gen.Index;

/**
 * Called when /accept executed. Cancel prevents enter accepting.
 */
public interface AccountEnterAcceptEvent extends AccountEvent, CancellableEvent, LinkEvent {
    @Index(4)
    LinkEntryUser getEntryUser();

    @Index(5)
    MessageableCommandActor getActor();
}
