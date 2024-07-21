package me.mastercapexd.auth.config.telegram;

import java.util.Collections;
import java.util.List;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.link.TelegramSettings;
import com.bivashy.auth.api.config.link.command.LinkCustomCommands;
import com.bivashy.auth.api.config.link.command.LinkDispatchCommandsSettings;
import com.bivashy.auth.api.config.link.stage.LinkConfirmationSettings;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.link.user.info.impl.UserNumberIdentificator;
import com.bivashy.auth.api.type.LinkConfirmationType;
import com.bivashy.configuration.ConfigurationHolder;
import com.bivashy.configuration.annotation.ConfigField;
import com.bivashy.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.link.*;
import me.mastercapexd.auth.config.message.telegram.TelegramMessages;

public class BaseTelegramSettings implements ConfigurationHolder, TelegramSettings {
    @ConfigField("enabled")
    private boolean enabled = false;
    @ConfigField("token")
    private String token;
    @ConfigField("confirmation")
    private BaseConfirmationSettings confirmationSettings;
    @ConfigField("restore")
    private BaseRestoreSettings restoreSettings;
    @ConfigField("enter")
    private BaseEnterSettings enterSettings;
    @ConfigField("telegram-commands")
    private BaseCommandPaths commandPaths;
    @ConfigField("proxy-commands")
    private BaseCommandPaths proxyCommandPaths;
    @ConfigField("dispatch-commands-after-link")
    private BaseDispatchCommandsSettings dispatchCommandsSettings = new BaseDispatchCommandsSettings();
    @ConfigField("custom-commands")
    private BaseMessengerCustomCommands commands;
    @ConfigField("max-telegram-link")
    private int maxTelegramLinkCount = 0;
    @ConfigField("telegram-messages")
    private TelegramMessages messages;
    @ConfigField("keyboards")
    private TelegramKeyboards keyboards;
    @ConfigField("admin-accounts")
    private List<Number> adminAccounts;
    @ConfigField("link-confirm-ways")
    private List<LinkConfirmationType> linkConfirmationTypes = Collections.singletonList(LinkConfirmationType.FROM_LINK);

    public BaseTelegramSettings() {
    }

    public BaseTelegramSettings(ConfigurationSectionHolder sectionHolder) {
        AuthPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
        if (token == null && enabled)
            System.err.println("Telegram bot token not found!");
    }

    @Override
    public String getBotToken() {
        return token;
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
    public BaseEnterSettings getEnterSettings() {
        return enterSettings;
    }

    @Override
    public boolean isAdministrator(LinkUserIdentificator identificator) {
        if (identificator == null || !identificator.isNumber())
            return false;
        return adminAccounts.stream().anyMatch(number -> number.longValue() == identificator.asNumber());
    }

    public boolean isAdministrator(long userId) {
        return isAdministrator(new UserNumberIdentificator(userId));
    }

    @Override
    public BaseRestoreSettings getRestoreSettings() {
        return restoreSettings;
    }

    @Override
    public BaseCommandPaths getCommandPaths() {
        return commandPaths;
    }

    @Override
    public BaseCommandPaths getProxyCommandPaths() {
        return proxyCommandPaths;
    }

    @Override
    public LinkDispatchCommandsSettings getDispatchCommandsSettings() {
        return dispatchCommandsSettings;
    }

    @Override
    public int getMaxLinkCount() {
        return maxTelegramLinkCount;
    }

    @Override
    public TelegramMessages getMessages() {
        return messages;
    }

    @Override
    public TelegramKeyboards getKeyboards() {
        return keyboards;
    }

    @Override
    public List<LinkConfirmationType> getLinkConfirmationTypes() {
        return Collections.unmodifiableList(linkConfirmationTypes);
    }

    @Override
    public boolean shouldDisableConversationCommands() {
        return false;
    }
}
