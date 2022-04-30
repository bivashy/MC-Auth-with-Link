package me.mastercapexd.auth.authentication.step.steps.link;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.account.factories.AccountFactory;
import me.mastercapexd.auth.authentication.step.AbstractAuthenticationStep;
import me.mastercapexd.auth.authentication.step.AuthenticationStep;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.creators.AbstractAuthenticationStepCreator;
import me.mastercapexd.auth.link.entryuser.LinkEntryUser;
import me.mastercapexd.auth.link.entryuser.vk.VKLinkEntryUser;
import me.mastercapexd.auth.link.message.keyboard.IKeyboard;
import me.mastercapexd.auth.link.user.LinkUser;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;
import me.mastercapexd.auth.link.vk.VKLinkType;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class VKLinkAuthenticationStep extends AbstractAuthenticationStep {
	private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();
	public static final String STEP_NAME = "VK_LINK";
	private final LinkEntryUser entryUser;

	public VKLinkAuthenticationStep(AuthenticationStepContext context) {
		super(STEP_NAME, context);
		entryUser = new VKLinkEntryUser(context.getAccount());
	}

	@Override
	public boolean shouldPassToNextStep() {
		return entryUser.isConfirmed();
	}

	@Override
	public boolean shouldSkip() {
		Account account = authenticationStepContext.getAccount();

		if (!PLUGIN.getConfig().getVKSettings().isEnabled()) // Ignore if vk was disabled in configuration
			return true;

		if (Auth.getLinkEntryAuth().hasLinkUser(account.getId(), VKLinkType.getInstance())) // Ignore if user already
																							// confirming
			return true;

		if (account.isSessionActive(PLUGIN.getConfig().getSessionDurability())) // Ignore if player has active session
			return true;

		LinkUser linkUser = account.findFirstLinkUser(VKLinkType.LINK_USER_FILTER).orElse(null);

		if (linkUser == null)
			return true;

		LinkUserInfo linkUserInfo = linkUser.getLinkUserInfo();

		if (linkUserInfo == null || linkUserInfo.getIdentificator().asNumber() == AccountFactory.DEFAULT_VK_ID
				|| !linkUserInfo.getConfirmationState().shouldSendConfirmation())
			return true;

		Auth.getLinkEntryAuth().addLinkUser(entryUser);

		IKeyboard keyboard = PLUGIN.getConfig().getVKSettings().getKeyboards().createKeyboard("confirmation", "%name%",
				account.getName());
		VKLinkType.getInstance().newMessageBuilder(PLUGIN.getConfig().getVKSettings().getMessages().getMessage("enter-message"))
				.keyboard(keyboard).build().send(linkUser);
		return false;
	}

	public static class VKLinkAuthenticationStepCreator extends AbstractAuthenticationStepCreator {
		public VKLinkAuthenticationStepCreator() {
			super(STEP_NAME);
		}

		@Override
		public AuthenticationStep createNewAuthenticationStep(AuthenticationStepContext context) {
			return new VKLinkAuthenticationStep(context);
		}
	}
}
