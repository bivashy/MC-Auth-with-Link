package com.bivashy.auth.api.provider;

import java.util.Collection;
import java.util.Optional;

import com.bivashy.auth.api.link.LinkType;

public interface LinkTypeProvider {
    void putLinkType(LinkType linkType);

    void putLinkType(String linkName, LinkType linkType);

    Optional<LinkType> getLinkType(String name);

    Collection<LinkType> getLinkTypes();
}
