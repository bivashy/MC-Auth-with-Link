package me.mastercapexd.auth.vk.commands;

import com.ubivashka.vk.bungee.events.VKMessageEvent;

import me.mastercapexd.auth.vk.commandhandler.VKCommandExecutor;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;

public class VKAdminPanelCommand extends VKCommandExecutor {
	private final VKReceptioner receptioner;

	public VKAdminPanelCommand(VKReceptioner receptioner) {
		this.receptioner = receptioner;
	}

	@Override
	public void execute(VKMessageEvent e, String[] args) {
		if (!receptioner.getConfig().getVKSettings().isAdminUser(e.getUserId()))
			return;
		receptioner.getPlugin().getVkUtils().sendAdminPanel(e.getUserId());
	}
}
