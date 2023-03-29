package me.mastercapexd.auth.link.user.confirmation;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.link.user.confirmation.LinkConfirmationUser;
import com.bivashy.auth.api.type.LinkConfirmationType;

public class BaseLinkConfirmationUser implements LinkConfirmationUser {
    private final LinkConfirmationType linkConfirmationType;
    private final long linkTimeoutTimestamp;
    private final Account target;
    private final String code;

    public BaseLinkConfirmationUser(LinkConfirmationType linkConfirmationType, long linkTimeoutTimestamp, Account target, String code) {
        this.linkConfirmationType = linkConfirmationType;
        this.linkTimeoutTimestamp = linkTimeoutTimestamp;
        this.target = target;
        this.code = code;
    }

    @Override
    public String getConfirmationCode() {
        return code;
    }

    @Override
    public LinkConfirmationType getLinkConfirmationType() {
        return linkConfirmationType;
    }

    @Override
    public Account getLinkTarget() {
        return target;
    }

    @Override
    public long getLinkTimeoutTimestamp() {
        return linkTimeoutTimestamp;
    }
}
