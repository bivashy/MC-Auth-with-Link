package me.mastercapexd.auth.vk.buttons;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.function.Predicate;

import com.ubivashka.vk.bungee.events.VKCallbackButtonPressEvent;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.VKEnterAnswer;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.link.entryuser.LinkEntryUser;
import me.mastercapexd.auth.link.vk.VKLinkType;
import me.mastercapexd.auth.vk.buttonshandler.VKButtonExecutor;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;

public class VKEnterButton implements VKButtonExecutor {
	private static final AuthPlugin PLUGIN = AuthPlugin.getInstance();
	private final VKReceptioner receptioner;

	public VKEnterButton(VKReceptioner receptioner) {
		this.receptioner = receptioner;
	}

	@Override
	public void execute(VKCallbackButtonPressEvent e, String payload) {
		Predicate<LinkEntryUser> filter = entryUser -> entryUser.getLinkUserInfo().getLinkUserId()
				.equals(e.getButtonEvent().getUserID()) && entryUser.getLinkType().equals(VKLinkType.getInstance())
				&& Duration.of(System.currentTimeMillis() - entryUser.getConfirmationStartTime(), ChronoUnit.MILLIS)
						.getSeconds() <= receptioner.getConfig().getVKSettings().getEnterSettings().getEnterDelay();

		if (!Auth.getLinkEntryAuth().hasLinkUser(filter))
			return;
		VKEnterAnswer answer = VKEnterAnswer.getByPayload(payload.split("_")[0]);
		LinkEntryUser entryAccount = Auth.getLinkEntryAuth().getLinkUsers(filter).stream().findFirst().orElse(null);
		if (entryAccount == null)
			return;
		if (answer == VKEnterAnswer.CONFIRM) {
			Auth.getLinkEntryAuth().removeLinkUsers((user) -> {
				user.setConfirmed(true);
				return filter.test(user);
			});
			Account account = entryAccount.getAccount();
			String stepName = PLUGIN.getConfig()
					.getAuthenticationStepName(account.getCurrentConfigurationAuthenticationStepCreatorIndex());
			account.nextAuthenticationStep(
					PLUGIN.getAuthenticationContextFactoryDealership().createContext(stepName, account));
		}
	}

}
