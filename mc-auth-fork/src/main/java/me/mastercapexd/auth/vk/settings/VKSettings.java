package me.mastercapexd.auth.vk.settings;

import java.util.List;

import net.md_5.bungee.config.Configuration;

public class VKSettings {
	private final boolean enabled;
	private VKConfirmationSettings confirmationSettings;
	private VKRestoreSettings restoreSettings;
	private VKEnterSettings enterSettings;
	private VKMainCommands mainCommands;
	private VKCommands commands;
	private List<Integer> adminAccounts;

	public VKSettings(boolean enabled, Configuration section) {
		this.enabled = enabled;
		if (!enabled)
			return;

		this.confirmationSettings = new VKConfirmationSettings(section.getSection("confirmation"));
		this.restoreSettings = new VKRestoreSettings(section.getSection("restore"));
		this.enterSettings = new VKEnterSettings(section.getSection("enter"));
		this.commands = new VKCommands(section.getSection("commands"));
		this.mainCommands = new VKMainCommands(section.getSection("vk-commands"));
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

	public VKRestoreSettings getRestoreSettings() {
		return restoreSettings;
	}

	public VKMainCommands getMainCommands() {
		return mainCommands;
	}

}
