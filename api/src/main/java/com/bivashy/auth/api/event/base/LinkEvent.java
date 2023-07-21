package com.bivashy.auth.api.event.base;

import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.LinkUser;

import io.github.revxrsal.eventbus.gen.Index;

public interface LinkEvent {
    @Index(2)
    LinkType getLinkType();

    @Index(3)
    LinkUser getLinkUser();

}
