package me.mastercapexd.auth.vk.commands;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Predicate;

import com.ubivashka.vk.bungee.events.VKMessageEvent;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.link.entryuser.LinkEntryUser;
import me.mastercapexd.auth.link.vk.VKLinkType;
import me.mastercapexd.auth.vk.commandhandler.VKCommandExecutor;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;

public class VKEnterDeclineCommand extends VKCommandExecutor {
	private final VKReceptioner receptioner;

	public VKEnterDeclineCommand(VKReceptioner receptioner) {
		this.receptioner = receptioner;
	}

	@Override
	public void execute(VKMessageEvent e, String[] args) {
		Predicate<LinkEntryUser> filter = entryUser -> entryUser.getLinkUserInfo().getLinkUserId()
				.equals(e.getUserId()) && entryUser.getLinkType().equals(VKLinkType.getInstance())
				&& Duration.of(System.currentTimeMillis() - entryUser.getConfirmationStartTime(), ChronoUnit.MILLIS)
						.getSeconds() > receptioner.getConfig().getVKSettings().getEnterSettings().getEnterDelay();
		
		if (isChat(e.getPeer()))
			return;
		List<LinkEntryUser> accounts = Auth.getLinkEntryAuth().getLinkUsers(filter);
		if (accounts.isEmpty())
			sendMessage(e.getPeer(), receptioner.getConfig().getVKSettings().getVKMessages().getMessage("enter-no-enter"));
		
		//for (VKEntryAccount account : accounts)
			//account.enterConnect(VKEnterAnswer.CONFIRM, receptioner.getConfig(), receptioner.getAccountStorage());
	}

	@Override
	public String getKey() {
		return "enter-decline";
	}

}
