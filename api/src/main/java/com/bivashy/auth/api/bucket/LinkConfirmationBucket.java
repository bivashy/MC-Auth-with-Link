package com.bivashy.auth.api.bucket;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

import com.bivashy.auth.api.link.user.confirmation.LinkConfirmationUser;

public interface LinkConfirmationBucket {
    Collection<LinkConfirmationUser> getConfirmationUsers();

    void addLinkConfirmation(LinkConfirmationUser user);

    void removeLinkConfirmation(LinkConfirmationUser user);

    default Optional<LinkConfirmationUser> findFirst(Predicate<LinkConfirmationUser> filter) {
        return getConfirmationUsers().stream().filter(filter).findFirst();
    }
}
