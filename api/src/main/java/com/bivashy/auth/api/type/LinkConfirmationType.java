package com.bivashy.auth.api.type;

import com.bivashy.auth.api.config.message.Messages;
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

        @Override
        public Messages<?> getConfirmationMessages(LinkConfirmationUser confirmationUser) {
            return confirmationUser.getLinkType().getServerMessages();
        }
    }, FROM_LINK {
        @Override
        public LinkUserIdentificator selectLinkUserIdentificator(LinkConfirmationUser confirmationUser, LinkUserIdentificator identificator) {
            return identificator;
        }

        @Override
        public Messages<?> getConfirmationMessages(LinkConfirmationUser confirmationUser) {
            return confirmationUser.getLinkType().getLinkMessages();
        }
    };

    public LinkConfirmationUser bindLinkConfirmationUser(LinkConfirmationUser confirmationUser, LinkUserIdentificator identificator) {
        return confirmationUser;
    }

    public abstract LinkUserIdentificator selectLinkUserIdentificator(LinkConfirmationUser confirmationUser, LinkUserIdentificator identificator);

    public abstract Messages<?> getConfirmationMessages(LinkConfirmationUser confirmationUser);
}
