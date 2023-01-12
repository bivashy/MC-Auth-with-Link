package me.mastercapexd.auth.link.entryuser;

import me.mastercapexd.auth.link.user.LinkUser;

public interface LinkEntryUser extends LinkUser {

    /**
     * @return UNIX timestamp when ILinkInfo instance created or link account
     * confirmation started
     */
    long getConfirmationStartTime();

    boolean isConfirmed();

    void setConfirmed(boolean confirmed);
}
