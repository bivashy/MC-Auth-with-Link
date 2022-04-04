package me.mastercapexd.auth.vk.commands;

import java.util.function.Predicate;

import com.ubivashka.vk.bungee.events.VKMessageEvent;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.account.factories.AccountFactory;
import me.mastercapexd.auth.bungee.events.VKLinkEvent;
import me.mastercapexd.auth.link.confirmation.LinkConfirmationUser;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;
import me.mastercapexd.auth.link.vk.VKLinkType;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.vk.commandhandler.VKCommandExecutor;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;
import net.md_5.bungee.api.ProxyServer;

public class VKLinkCommand extends VKCommandExecutor {
	private VKReceptioner receptioner;

	public VKLinkCommand(VKReceptioner receptioner) {
		this.receptioner = receptioner;
	}

	@Override
	public void execute(VKMessageEvent e, String[] args) {
		if (isChat(e.getPeer()))
			return;
		if (args.length < 1) {
			sendMessage(e.getPeer(), receptioner.getConfig().getVKSettings().getVKMessages()
					.getMessage("confirmation-not-enough-arguments"));
			return;
		}

		Predicate<LinkConfirmationUser> filter = linkUser -> linkUser.getLinkType().equals(VKLinkType.getInstance())
				&& linkUser.getLinkUserInfo().getLinkUserId().intValue() == e.getUserId().intValue();

		String code = args[0];
		LinkConfirmationUser confirmationUser = Auth.getLinkConfirmationAuth().getLinkUsers(filter).stream().findFirst()
				.orElse(null);
		if (confirmationUser == null) {
			sendMessage(e.getPeer(),
					receptioner.getConfig().getVKSettings().getVKMessages().getMessage("confirmation-no-code"));
			return;
		}
		if (!confirmationUser.getConfirmationInfo().getConfirmationCode().equals(code)) {
			sendMessage(e.getPeer(),
					receptioner.getConfig().getVKSettings().getVKMessages().getMessage("confirmation-error"));
			return;
		}

		receptioner.getAccountStorage().getAccount(confirmationUser.getAccount().getId()).thenAccept(account -> {
			LinkUserInfo vkLinkInfo = account.findFirstLinkUser(VKLinkType.getLinkUserPredicate()).orElse(null)
					.getLinkUserInfo();

			if (vkLinkInfo.getLinkUserId() != AccountFactory.DEFAULT_VK_ID) {
				sendMessage(e.getPeer(), receptioner.getConfig().getVKSettings().getVKMessages()
						.getMessage("confirmation-already-linked"));
				return;
			}
			VKLinkEvent event = new VKLinkEvent(e.getUserId(), account);
			ProxyServer.getInstance().getPluginManager().callEvent(event);
			if (event.isCancelled())
				return;
			account.findFirstLinkUser(VKLinkType.getLinkUserPredicate()).orElse(null).getLinkUserInfo()
					.setLinkUserId(e.getUserId());
			receptioner.getAccountStorage().saveOrUpdateAccount(account);
			ProxyPlayer player = receptioner.getConfig().getActiveIdentifierType().getPlayer(account.getId());
			if (player != null)
				player.sendMessage(receptioner.getConfig().getBungeeMessages().getStringMessage("vk-linked"));

			sendMessage(e.getPeer(),
					receptioner.getConfig().getVKSettings().getVKMessages().getMessage("confirmation-success"));
			Auth.getLinkConfirmationAuth().removeLinkUsers(filter);
		});

	}

	@Override
	public String getKey() {
		return "code";
	}
}
