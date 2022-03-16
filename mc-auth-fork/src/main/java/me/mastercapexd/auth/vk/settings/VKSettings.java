package me.mastercapexd.auth.vk.settings;

import java.util.List;

import com.ubivashka.config.annotations.ConfigField;
import com.ubivashka.config.processors.BungeeConfigurationHolder;

import me.mastercapexd.auth.config.VKButtonLabels;
import me.mastercapexd.auth.config.messages.vk.VKMessages;
import net.md_5.bungee.config.Configuration;

public class VKSettings extends BungeeConfigurationHolder {
	@ConfigField
	private boolean enabled = false;
	@ConfigField(path = "confirmation")
	private VKConfirmationSettings confirmationSettings = null;
	@ConfigField(path = "restore")
	private VKRestoreSettings restoreSettings  = null;
	@ConfigField(path = "enter")
	private VKEnterSettings enterSettings = null;
	@ConfigField(path = "vk-commands")
	private VKCommandPaths commandPaths = null;
	@ConfigField(path = "commands")
	private VKCommands commands = null;
	@ConfigField(path = "max-vk-link")
	private Integer maxVkLinkCount = 0;
	@ConfigField(path = "vkmessages")
	private VKMessages messages = null;
	@ConfigField(path = "button-labels")
	private VKButtonLabels buttonLabels = null;
	@ConfigField(path = "admin-accounts")
	private List<Integer> adminAccounts = null;
	public VKSettings() {
	}
	
	public VKSettings(Configuration section) {
		init(section);
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

	public VKCommandPaths getCommandPaths() {
		return commandPaths;
	}

	public int getMaxLinkCount() {
		return maxVkLinkCount;
	}

	public VKMessages getVKMessages() {
		return messages;
	}

	public VKButtonLabels getVKButtonLabels() {
		return buttonLabels;
	}
}
