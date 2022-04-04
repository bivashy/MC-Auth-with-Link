package me.mastercapexd.auth.messenger.commands.parameters;

import me.mastercapexd.auth.link.confirmation.LinkConfirmationUser;

public class MessengerLinkContext {
	private final LinkConfirmationUser confirmationUser;
	private final String linkCode;
	private final Runnable successAction;

	public MessengerLinkContext(String linkCode, LinkConfirmationUser confirmationUser, Runnable successAction) {
		this.confirmationUser = confirmationUser;
		this.linkCode = linkCode;
		this.successAction = successAction;
	}

	public String getLinkCode() {
		return linkCode;
	}

	public LinkConfirmationUser getConfirmationUser() {
		return confirmationUser;
	}

	public void onSuccess() {
		successAction.run();
	}
}
