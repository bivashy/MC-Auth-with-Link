package me.mastercapexd.auth.config.messenger;

public interface MessengerConfirmationSettings {
	int getRemoveDelay();

	int getCodeLength();

	boolean canToggleConfirmation();
}
