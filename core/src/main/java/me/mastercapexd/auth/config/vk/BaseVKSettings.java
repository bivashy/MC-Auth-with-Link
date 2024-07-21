package me.mastercapexd.auth.config.vk;

import java.util.Collections;
import java.util.List;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.link.LinkKeyboards;
import com.bivashy.auth.api.config.link.VKSettings;
import com.bivashy.auth.api.config.link.command.LinkCommandPaths;
import com.bivashy.auth.api.config.link.command.LinkCustomCommands;
import com.bivashy.auth.api.config.link.command.LinkDispatchCommandsSettings;
import com.bivashy.auth.api.config.link.stage.LinkConfirmationSettings;
import com.bivashy.auth.api.config.link.stage.LinkEnterSettings;
import com.bivashy.auth.api.config.link.stage.LinkRestoreSettings;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.link.user.info.impl.UserNumberIdentificator;
import com.bivashy.auth.api.type.LinkConfirmationType;
import com.bivashy.configuration.ConfigurationHolder;
import com.bivashy.configuration.annotation.ConfigField;
import com.bivashy.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.link.*;
import me.mastercapexd.auth.config.message.link.LinkMessages;
import me.mastercapexd.auth.config.message.vk.VKMessages;

public class BaseVKSettings implements ConfigurationHolder, VKSettings {
    @ConfigField("enabled")
    private boolean enabled = false;
    @ConfigField("confirmation")
    private BaseConfirmationSettings confirmationSettings;
    @ConfigField("restore")
    private BaseRestoreSettings restoreSettings;
    @ConfigField("enter")
    private BaseEnterSettings enterSettings;
    @ConfigField("vk-commands")
    private BaseCommandPaths commandPaths;
    @ConfigField("proxy-commands")
    private BaseCommandPaths proxyCommandPaths;
    @ConfigField("dispatch-commands-after-link")
    private BaseDispatchCommandsSettings dispatchCommandsSettings = new BaseDispatchCommandsSettings();
    @ConfigField("custom-commands")
    private BaseMessengerCustomCommands commands;
    @ConfigField("max-vk-link")
    private int maxVkLinkCount = 0;
    @ConfigField("vk-messages")
    private VKMessages messages;
    @ConfigField("keyboards")
    private VKKeyboards keyboards;
    @ConfigField("admin-accounts")
    private List<Integer> adminAccounts;
    @ConfigField("disable-conversation-commands")
    private boolean disableConversationCommands;
    @ConfigField("link-confirm-ways")
    private List<LinkConfirmationType> linkConfirmationTypes = Collections.singletonList(LinkConfirmationType.FROM_LINK);
    @ConfigField("link-game-commands")
    private List<String> gameLinkCommands;

    public BaseVKSettings() {
    }

    public BaseVKSettings(ConfigurationSectionHolder sectionHolder) {
        AuthPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public LinkConfirmationSettings getConfirmationSettings() {
        return confirmationSettings;
    }

    @Override
    public LinkCustomCommands getCustomCommands() {
        return commands;
    }

    @Override
    public LinkEnterSettings getEnterSettings() {
        return enterSettings;
    }

    @Override
    public boolean isAdministrator(LinkUserIdentificator identificator) {
        if (identificator == null || !identificator.isNumber())
            return false;
        return adminAccounts.contains((int) identificator.asNumber());
    }

    public boolean shouldDisableConversationCommands() {
        return disableConversationCommands;
    }

    public boolean isAdministrator(int userId) {
        return isAdministrator(new UserNumberIdentificator(userId));
    }

    @Override
    public LinkRestoreSettings getRestoreSettings() {
        return restoreSettings;
    }

    @Override
    public LinkCommandPaths getCommandPaths() {
        return commandPaths;
    }

    @Override
    public LinkCommandPaths getProxyCommandPaths() {
        return proxyCommandPaths;
    }

    @Override
    public LinkDispatchCommandsSettings getDispatchCommandsSettings() {
        return dispatchCommandsSettings;
    }

    @Override
    public int getMaxLinkCount() {
        return maxVkLinkCount;
    }

    @Override
    public LinkMessages getMessages() {
        return messages;
    }

    @Override
    public List<LinkConfirmationType> getLinkConfirmationTypes() {
        return Collections.unmodifiableList(linkConfirmationTypes);
    }

    @Override
    public LinkKeyboards getKeyboards() {
        return keyboards;
    }
}
