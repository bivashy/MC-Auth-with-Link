package me.mastercapexd.auth.vk.commands;

import com.ubivashka.vk.bungee.events.VKMessageEvent;

import me.mastercapexd.auth.vk.builders.AdminPanelBuilder;
import me.mastercapexd.auth.vk.commandhandler.VKCommandExecutor;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;

public class VKAdminPanelCommand extends VKCommandExecutor {
	private final VKReceptioner receptioner;

	public VKAdminPanelCommand(VKReceptioner receptioner) {
		this.receptioner = receptioner;
	}

	@Override
	public void execute(VKMessageEvent e, String[] args) {
		if (!receptioner.getConfig().getVKSettings().isAdministrator(e.getUserId()))
			return;
		new AdminPanelBuilder(e.getUserId(), receptioner).execute();
	}

	@Override
	public String getKey() {
		return "admin-panel";
	}
}
