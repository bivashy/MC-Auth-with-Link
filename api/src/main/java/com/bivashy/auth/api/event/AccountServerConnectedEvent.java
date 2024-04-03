package com.bivashy.auth.api.event;

import com.bivashy.auth.api.event.base.AccountEvent;
import com.bivashy.auth.api.server.proxy.ProxyServer;
import io.github.revxrsal.eventbus.gen.Index;

/**
 * Called when Account(Player) joined sub-server.
 */
public interface AccountServerConnectedEvent extends AccountEvent {

    @Index(1)
    ProxyServer getServer();
}
