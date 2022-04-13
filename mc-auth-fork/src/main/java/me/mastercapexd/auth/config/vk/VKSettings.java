package me.mastercapexd.auth.config.vk;

import java.util.List;

import com.ubivashka.configuration.annotations.ConfigField;
import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.ConfigurationHolder;
import me.mastercapexd.auth.config.messages.vk.VKMessages;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class VKSettings implements ConfigurationHolder {
	@ConfigField
	private boolean enabled = false;
	@ConfigField("confirmation")
	private VKConfirmationSettings confirmationSettings = null;
	@ConfigField("restore")
	private VKRestoreSettings restoreSettings = null;
	@ConfigField("enter")
	private VKEnterSettings enterSettings = null;
	@ConfigField("vk-commands")
	private VKCommandPaths commandPaths = null;
	@ConfigField("custom-commands")
	private VKCommands commands = null;
	@ConfigField("max-vk-link")
	private Integer maxVkLinkCount = 0;
	@ConfigField("vk-messages")
	private VKMessages messages = null;
	@ConfigField("button-labels")
	private VKButtonLabels buttonLabels = null;
	@ConfigField("admin-accounts")
	private List<Integer> adminAccounts = null;

	public VKSettings() {
	}

	public VKSettings(ConfigurationSectionHolder sectionHolder) {
		ProxyPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
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
