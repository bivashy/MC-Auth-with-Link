package com.bivashy.auth.api.bucket;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.LinkUser;

public interface LinkAuthenticationBucket<T extends LinkUser> extends Bucket<T> {

    default Predicate<T> createFilter(String accountId, LinkType linkType) {
        return linkUser -> linkUser.getAccount().getPlayerId().equals(accountId) && linkUser.getLinkType().equals(linkType);
    }

    default Optional<T> find(String id, LinkType linkType) {
        return findFirst(createFilter(id, linkType));
    }

    @Deprecated
    default boolean hasLinkUser(Predicate<T> filter) {
        return has(filter);
    }

    @Deprecated
    default boolean hasLinkUser(String id, LinkType linkType) {
        return has(createFilter(id, linkType));
    }

    @Deprecated
    default void addLinkUser(T linkUser) {
        modifiable().add(linkUser);
    }

    default void removeLinkUsers(Predicate<T> filter) {
        modifiable().removeIf(filter);
    }

    @Deprecated
    default void removeLinkUser(String id, LinkType linkType) {
        modifiable().removeIf(createFilter(id, linkType));
    }

    @Deprecated
    default void removeLinkUser(T linkUser) {
        modifiable().remove(linkUser);
    }

    @Deprecated
    default Optional<T> findFirstLinkUser(Predicate<T> filter) {
        return findFirst(filter);
    }

    @Deprecated
    default List<T> getLinkUsers(Predicate<T> filter) {
        return find(filter);
    }

    @Deprecated
    default T getLinkUser(String id, LinkType linkType) {
        return findFirst(createFilter(id, linkType)).orElse(null);
    }

}
