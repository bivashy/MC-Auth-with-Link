package me.mastercapexd.auth.link.confirmation.info;

public class DefaultConfirmationInfo extends AbstractLinkConfirmationInfo {

	private final String confirmationCode;

	public DefaultConfirmationInfo(Integer linkUserId, String confirmationCode) {
		super(linkUserId);
		this.confirmationCode = confirmationCode;
	}

	@Override
	public String getConfirmationCode() {
		return confirmationCode;
	}

}
