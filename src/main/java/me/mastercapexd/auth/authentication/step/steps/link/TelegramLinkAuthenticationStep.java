package me.mastercapexd.auth.authentication.step.steps.link;

import com.ubivaska.messenger.common.identificator.Identificator;
import com.ubivaska.messenger.common.keyboard.Keyboard;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.account.factories.AccountFactory;
import me.mastercapexd.auth.authentication.step.AbstractAuthenticationStep;
import me.mastercapexd.auth.authentication.step.AuthenticationStep;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.creators.AbstractAuthenticationStepCreator;
import me.mastercapexd.auth.link.entryuser.LinkEntryUser;
import me.mastercapexd.auth.link.entryuser.telegram.TelegramLinkEntryUser;
import me.mastercapexd.auth.link.telegram.TelegramLinkType;
import me.mastercapexd.auth.link.user.LinkUser;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class TelegramLinkAuthenticationStep extends AbstractAuthenticationStep {
	private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();
	public static final String STEP_NAME = "TELEGRAM_LINK";
	private final LinkEntryUser entryUser;

	public TelegramLinkAuthenticationStep(AuthenticationStepContext context) {
		super(STEP_NAME, context);
		entryUser = new TelegramLinkEntryUser(context.getAccount());
	}

	@Override
	public boolean shouldPassToNextStep() {
		return entryUser.isConfirmed();
	}

	@Override
	public boolean shouldSkip() {
		Account account = authenticationStepContext.getAccount();

		if (!PLUGIN.getConfig().getTelegramSettings().isEnabled()) // Ignore if telegram was disabled in configuration
			return true;

		if (Auth.getLinkEntryAuth().hasLinkUser(account.getId(), TelegramLinkType.getInstance())) // Ignore if user
																									// already
			// confirming
			return true;

		if (account.isSessionActive(PLUGIN.getConfig().getSessionDurability())) // Ignore if player has active session
			return true;

		LinkUser linkUser = account.findFirstLinkUser(TelegramLinkType.LINK_USER_FILTER).orElse(null);

		if (linkUser == null)
			return true;

		LinkUserInfo linkUserInfo = linkUser.getLinkUserInfo();

		if (linkUserInfo == null || linkUserInfo.getIdentificator().asNumber() == AccountFactory.DEFAULT_TELEGRAM_ID
				|| !linkUserInfo.getConfirmationState().shouldSendConfirmation())
			return true;

		Auth.getLinkEntryAuth().addLinkUser(entryUser);

		Keyboard keyboard = PLUGIN.getConfig().getTelegramSettings().getKeyboards().createKeyboard("confirmation",
				"%name%", account.getName());
		TelegramLinkType.getInstance()
				.newMessageBuilder(PLUGIN.getConfig().getTelegramSettings().getMessages().getMessage("enter-message", TelegramLinkType.getInstance().newMessageContext(account)))
				.keyboard(keyboard).build()
				.send(Identificator.of(linkUser.getLinkUserInfo().getIdentificator().asNumber()));
		return false;
	}

	public static class TelegramLinkAuthenticationStepCreator extends AbstractAuthenticationStepCreator {
		public TelegramLinkAuthenticationStepCreator() {
			super(STEP_NAME);
		}

		@Override
		public AuthenticationStep createNewAuthenticationStep(AuthenticationStepContext context) {
			return new TelegramLinkAuthenticationStep(context);
		}
	}
}
