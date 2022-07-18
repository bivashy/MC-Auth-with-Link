package me.mastercapexd.auth.config.vk;

import java.util.List;

import com.ubivashka.configuration.ConfigurationHolder;
import com.ubivashka.configuration.annotation.ConfigField;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.message.vk.VKMessages;
import me.mastercapexd.auth.config.messenger.DefaultCommandPaths;
import me.mastercapexd.auth.config.messenger.DefaultConfirmationSettings;
import me.mastercapexd.auth.config.messenger.DefaultEnterSettings;
import me.mastercapexd.auth.config.messenger.DefaultMessengerCustomCommands;
import me.mastercapexd.auth.config.messenger.DefaultRestoreSettings;
import me.mastercapexd.auth.config.messenger.MessengerCustomCommands;
import me.mastercapexd.auth.config.messenger.MessengerSettings;
import me.mastercapexd.auth.link.user.info.identificator.LinkUserIdentificator;
import me.mastercapexd.auth.link.user.info.identificator.UserNumberIdentificator;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class VKSettings implements ConfigurationHolder, MessengerSettings {
    @ConfigField("enabled")
    private boolean enabled = false;
    @ConfigField("confirmation")
    private DefaultConfirmationSettings confirmationSettings;
    @ConfigField("restore")
    private DefaultRestoreSettings restoreSettings;
    @ConfigField("enter")
    private DefaultEnterSettings enterSettings;
    @ConfigField("vk-commands")
    private DefaultCommandPaths commandPaths;
    @ConfigField("custom-commands")
    private DefaultMessengerCustomCommands commands;
    @ConfigField("max-vk-link")
    private Integer maxVkLinkCount = 0;
    @ConfigField("vk-messages")
    private VKMessages messages;
    @ConfigField("keyboards")
    private VKKeyboards keyboards;
    @ConfigField("admin-accounts")
    private List<Integer> adminAccounts;

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
    public DefaultConfirmationSettings getConfirmationSettings() {
        return confirmationSettings;
    }

    @Override
    public MessengerCustomCommands getCustomCommands() {
        return commands;
    }

    @Override
    public DefaultEnterSettings getEnterSettings() {
        return enterSettings;
    }

    @Override
    public boolean isAdministrator(LinkUserIdentificator identificator) {
        if (identificator == null || !identificator.isNumber())
            return false;
        return adminAccounts.contains((int) identificator.asNumber());
    }

    public boolean isAdministrator(int userId) {
        return isAdministrator(new UserNumberIdentificator(userId));
    }

    @Override
    public DefaultRestoreSettings getRestoreSettings() {
        return restoreSettings;
    }

    @Override
    public DefaultCommandPaths getCommandPaths() {
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

    @Override
    public VKKeyboards getKeyboards() {
        return keyboards;
    }
}
