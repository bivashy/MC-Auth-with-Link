package me.mastercapexd.auth.bucket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.bivashy.auth.api.bucket.LinkConfirmationBucket;
import com.bivashy.auth.api.link.user.confirmation.LinkConfirmationUser;

public class BaseLinkConfirmationBucket implements LinkConfirmationBucket {
    private final List<LinkConfirmationUser> confirmationUsers = new ArrayList<>();

    @Override
    public Collection<LinkConfirmationUser> getConfirmationUsers() {
        return Collections.unmodifiableCollection(confirmationUsers);
    }

    @Override
    public void addLinkConfirmation(LinkConfirmationUser user) {
        confirmationUsers.add(user);
    }

    @Override
    public void removeLinkConfirmation(LinkConfirmationUser user) {
        confirmationUsers.remove(user);
    }
}
