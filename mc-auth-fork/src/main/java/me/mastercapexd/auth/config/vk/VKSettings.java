package me.mastercapexd.auth.config.vk;

import java.util.List;

import com.ubivashka.configuration.annotations.ConfigField;
import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.ConfigurationHolder;
import me.mastercapexd.auth.config.messages.vk.VKMessages;
import me.mastercapexd.auth.config.messenger.MessengerSettings;
import me.mastercapexd.auth.link.user.info.identificator.LinkUserIdentificator;
import me.mastercapexd.auth.link.user.info.identificator.UserNumberIdentificator;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class VKSettings implements ConfigurationHolder, MessengerSettings {
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
	@ConfigField("keyboards")
	private VKKeyboards keyboards = null;
	@ConfigField("admin-accounts")
	private List<Integer> adminAccounts = null;

	public VKSettings() {
	}

	public VKSettings(ConfigurationSectionHolder sectionHolder) {
		ProxyPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public VKConfirmationSettings getConfirmationSettings() {
		return confirmationSettings;
	}

	public VKCommands getCommands() {
		return commands;
	}

	@Override
	public VKEnterSettings getEnterSettings() {
		return enterSettings;
	}

	@Override
	public boolean isAdministrator(LinkUserIdentificator identificator) {
		if (identificator == null || !identificator.isNumber())
			return false;
		return adminAccounts.contains(identificator.asNumber());
	}

	public boolean isAdministrator(int userId) {
		return isAdministrator(new UserNumberIdentificator(userId));
	}

	@Override
	public VKRestoreSettings getRestoreSettings() {
		return restoreSettings;
	}

	@Override
	public VKCommandPaths getCommandPaths() {
		return commandPaths;
	}

	@Override
	public int getMaxLinkCount() {
		return maxVkLinkCount;
	}

	@Override
	public VKMessages getMessages() {
		return messages;
	}

	public VKButtonLabels getVKButtonLabels() {
		return buttonLabels;
	}

	@Override
	public VKKeyboards getKeyboards() {
		return keyboards;
	}
}
