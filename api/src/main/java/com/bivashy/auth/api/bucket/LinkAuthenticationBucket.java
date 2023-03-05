package com.bivashy.auth.api.bucket;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.LinkUser;

public interface LinkAuthenticationBucket<T extends LinkUser> {
    boolean hasLinkUser(Predicate<T> filter);

    boolean hasLinkUser(String id, LinkType linkType);

    void addLinkUser(T linkUser);

    void removeLinkUsers(Predicate<T> filter);

    void removeLinkUser(String id, LinkType linkType);

    void removeLinkUser(T linkUser);

    Optional<T> findFirstLinkUser(Predicate<T> filter);

    List<T> getLinkUsers(Predicate<T> filter);

    T getLinkUser(String id, LinkType linkType);
}
