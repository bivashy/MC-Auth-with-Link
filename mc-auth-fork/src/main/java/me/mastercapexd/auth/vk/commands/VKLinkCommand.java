package me.mastercapexd.auth.vk.commands;

import com.ubivashka.vk.bungee.events.VKMessageEvent;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.bungee.events.VKLinkEvent;
import me.mastercapexd.auth.objects.VKConfirmationEntry;
import me.mastercapexd.auth.vk.commandhandler.VKCommandExecutor;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class VKLinkCommand extends VKCommandExecutor {
	private VKReceptioner receptioner;

	public VKLinkCommand(VKReceptioner receptioner) {
		this.receptioner = receptioner;
	}

	@Override
	public void execute(VKMessageEvent e, String[] args) {
		if (isChat(e.getPeer()))
			return;
		String[] arguments = e.getMessage().getText().split("\\s+");
		if (arguments.length < 2) {
			sendMessage(e.getPeer(),
					receptioner.getConfig().getVKMessages().getLegacyMessage("confirmation-not-enough-arguments"));
			return;
		}
		String code = arguments[1];
		VKConfirmationEntry entry = Auth.getVKConfirmationEntry(e.getUserId());
		if (entry == null) {
			sendMessage(e.getPeer(), receptioner.getConfig().getVKMessages().getLegacyMessage("confirmation-no-code"));
			return;
		}
		if (!entry.getCode().equals(code)) {
			sendMessage(e.getPeer(), receptioner.getConfig().getVKMessages().getLegacyMessage("confirmation-error"));
			return;
		}

		receptioner.getAccountStorage().getAccount(entry.getId()).thenAccept(account -> {
			if (account.getVKId() != -1) {
				sendMessage(e.getPeer(),
						receptioner.getConfig().getVKMessages().getLegacyMessage("confirmation-already-linked"));
				return;
			}
			VKLinkEvent event = new VKLinkEvent(e.getUserId(), account);
			ProxyServer.getInstance().getPluginManager().callEvent(event);
			if (event.isCancelled())
				return;
			account.setVKId(e.getUserId());
			receptioner.getAccountStorage().saveOrUpdateAccount(account);
			ProxiedPlayer player = receptioner.getConfig().getActiveIdentifierType().getPlayer(account.getId());
			if (player != null)
				player.sendMessage(receptioner.getConfig().getBungeeMessages().getMessage("vk-linked"));

			sendMessage(e.getPeer(), receptioner.getConfig().getVKMessages().getLegacyMessage("confirmation-success"));
			Auth.removeVKConfirmationEntry(e.getUserId());
		});

	}
}
