package me.mastercapexd.auth.link.user.confirmation;

import com.bivashy.auth.api.link.user.confirmation.LinkConfirmationInfo;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;

import me.mastercapexd.auth.link.user.BaseLinkUserInfo;

public class BaseConfirmationInfo extends BaseLinkUserInfo implements LinkConfirmationInfo {
    private final String confirmationCode;

    public BaseConfirmationInfo(LinkUserIdentificator userIdentificator, String confirmationCode) {
        super(userIdentificator);
        this.confirmationCode = confirmationCode;
    }

    @Override
    public String getConfirmationCode() {
        return confirmationCode;
    }
}
