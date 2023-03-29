package com.bivashy.auth.api.type;

import com.bivashy.auth.api.link.user.confirmation.LinkConfirmationUser;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;

public enum LinkConfirmationType {
    FROM_GAME {
        @Override
        public LinkConfirmationUser bindLinkConfirmationUser(LinkConfirmationUser confirmationUser, LinkUserIdentificator identificator) {
            confirmationUser.setLinkUserIdentificator(identificator);
            return confirmationUser;
        }

        @Override
        public LinkUserIdentificator selectLinkUserIdentificator(LinkConfirmationUser confirmationUser, LinkUserIdentificator identificator) {
            return confirmationUser.getLinkUserIdentificator();
        }
    }, FROM_LINK {
        @Override
        public LinkUserIdentificator selectLinkUserIdentificator(LinkConfirmationUser confirmationUser, LinkUserIdentificator identificator) {
            return identificator;
        }
    };

    public LinkConfirmationUser bindLinkConfirmationUser(LinkConfirmationUser confirmationUser, LinkUserIdentificator identificator) {
        return confirmationUser;
    }

    public abstract LinkUserIdentificator selectLinkUserIdentificator(LinkConfirmationUser confirmationUser, LinkUserIdentificator identificator);
}
