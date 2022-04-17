package me.mastercapexd.auth.vk.commands;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Predicate;

import com.ubivashka.vk.bungee.events.VKMessageEvent;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.entryuser.LinkEntryUser;
import me.mastercapexd.auth.link.vk.VKLinkType;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.vk.commandhandler.VKCommandExecutor;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;

public class VKEnterAcceptCommand extends VKCommandExecutor {
	private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();
	private final VKReceptioner receptioner;

	public VKEnterAcceptCommand(VKReceptioner receptioner) {
		this.receptioner = receptioner;
	}

	@Override
	public void execute(VKMessageEvent e, String[] args) {
		Predicate<LinkEntryUser> filter = entryUser -> {
			return entryUser.getLinkUserInfo().getIdentificator().asNumber() == e.getUserId()
					&& entryUser.getLinkType().equals(VKLinkType.getInstance())
					&& Duration.of(System.currentTimeMillis() - entryUser.getConfirmationStartTime(), ChronoUnit.MILLIS)
							.getSeconds() <= receptioner.getConfig().getVKSettings().getEnterSettings().getEnterDelay();
		};

		if (isChat(e.getPeer()))
			return;
		List<LinkEntryUser> accounts = Auth.getLinkEntryAuth().getLinkUsers(filter);
		if (accounts.isEmpty()) {
			sendMessage(e.getPeer(),
					receptioner.getConfig().getVKSettings().getMessages().getMessage("enter-no-accounts"));
			return;
		}
		Auth.getLinkEntryAuth().removeLinkUsers((entryUser) -> {
			boolean isUserValid = filter.test(entryUser);
			if (isUserValid)
				entryUser.setConfirmed(true);
			return isUserValid;
		});

		Account account = accounts.stream().findFirst().orElse(null).getAccount();
		account.nextAuthenticationStep(
				PLUGIN.getAuthenticationContextFactoryDealership().createContext(account));
	}

	@Override
	public String getKey() {
		return "enter-accept";
	}

}
