package me.mastercapexd.auth.vk.settings;

import java.util.List;

import me.mastercapexd.auth.vk.commands.VKCommands;
import net.md_5.bungee.config.Configuration;

public class VKSettings {
	private final boolean enabled;
	private final VKConfirmationSettings confirmationSettings;
	private final VKEnterSettings enterSettings;
	private final VKCommands commands;
	private final List<Integer> adminAccounts;

	public VKSettings(boolean enabled, Configuration section) {
		this.enabled = enabled;
		this.confirmationSettings = new VKConfirmationSettings(section.getSection("confirmation"));
		this.enterSettings = new VKEnterSettings(section.getSection("enter"));
		this.commands = new VKCommands(section.getSection("commands"));
		this.adminAccounts = section.getIntList("admin-accounts");
	}

	public boolean isEnabled() {
		return enabled;
	}

	public VKConfirmationSettings getConfirmationSettings() {
		return confirmationSettings;
	}

	public VKCommands getCommands() {
		return commands;
	}

	public VKEnterSettings getEnterSettings() {
		return enterSettings;
	}

	public boolean isAdminUser(Integer userId) {
		if (userId == null)
			return false;
		return adminAccounts.contains(userId);
	}

}
