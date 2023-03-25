package me.mastercapexd.auth.link.user.confirmation;

import com.bivashy.auth.api.link.user.confirmation.LinkConfirmationInfo;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.type.LinkConfirmationType;

import me.mastercapexd.auth.link.user.BaseLinkUserInfo;

public class BaseConfirmationInfo extends BaseLinkUserInfo implements LinkConfirmationInfo {
    private final String confirmationCode;
    private final LinkConfirmationType linkConfirmationType;

    public BaseConfirmationInfo(LinkUserIdentificator userIdentificator, String confirmationCode, LinkConfirmationType linkConfirmationType) {
        super(userIdentificator);
        this.confirmationCode = confirmationCode;
        this.linkConfirmationType = linkConfirmationType;
    }

    @Override
    public String getConfirmationCode() {
        return confirmationCode;
    }

    @Override
    public LinkConfirmationType getLinkConfirmationType() {
        return linkConfirmationType;
    }
}
