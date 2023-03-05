package me.mastercapexd.auth.link.google;

import java.util.function.Predicate;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.account.AccountFactory;
import com.bivashy.auth.api.config.link.LinkSettings;
import com.bivashy.auth.api.config.message.Messages;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.LinkUser;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.link.user.info.impl.UserStringIdentificator;
import com.bivashy.auth.api.server.message.ServerComponent;
import com.ubivaska.messenger.common.button.Button.ButtonBuilder;
import com.ubivaska.messenger.common.button.ButtonAction.ButtonActionBuilder;
import com.ubivaska.messenger.common.button.ButtonColor.ButtonColorBuilder;
import com.ubivaska.messenger.common.keyboard.Keyboard.KeyboardBuilder;
import com.ubivaska.messenger.common.message.Message.MessageBuilder;

import me.mastercapexd.auth.config.message.link.context.LinkPlaceholderContext;

public class GoogleLinkType implements LinkType {
    private static final GoogleLinkType INSTANCE = new GoogleLinkType();
    public static final Predicate<LinkUser> LINK_USER_FILTER = (linkUser) -> linkUser.getLinkType() == getInstance();
    private static final AuthPlugin PLUGIN = AuthPlugin.instance();
    private static final LinkUserIdentificator DEFAULT_IDENTIFICATOR = new UserStringIdentificator(AccountFactory.DEFAULT_GOOGLE_KEY);
    private static final String CANNOT_CREATE_BUILDER_ERROR = "Can`t create builder with GoogleLinkType!";
    private static final String UNSUPPORTED_ERROR = "This method don`t supported by google link type";

    private GoogleLinkType() {
    }

    public static GoogleLinkType getInstance() {
        return INSTANCE;
    }

    @Override
    public MessageBuilder newMessageBuilder(String text) {
        throw new UnsupportedOperationException(CANNOT_CREATE_BUILDER_ERROR);
    }

    @Override
    public ButtonBuilder newButtonBuilder(String label) {
        throw new UnsupportedOperationException(CANNOT_CREATE_BUILDER_ERROR);
    }

    @Override
    public KeyboardBuilder newKeyboardBuilder() {
        throw new UnsupportedOperationException(CANNOT_CREATE_BUILDER_ERROR);
    }

    @Override
    public ButtonColorBuilder newButtonColorBuilder() {
        throw new UnsupportedOperationException(CANNOT_CREATE_BUILDER_ERROR);
    }

    @Override
    public ButtonActionBuilder newButtonActionBuilder() {
        throw new UnsupportedOperationException(CANNOT_CREATE_BUILDER_ERROR);
    }

    @Override
    public LinkPlaceholderContext newMessageContext(Account account) {
        throw new UnsupportedOperationException(UNSUPPORTED_ERROR);
    }

    @Override
    public Messages<ServerComponent> getServerMessages() {
        return PLUGIN.getConfig().getServerMessages().getSubMessages("google");
    }

    @Override
    public Messages<String> getLinkMessages() {
        throw new UnsupportedOperationException(UNSUPPORTED_ERROR);
    }

    @Override
    public LinkSettings getSettings() {
        throw new UnsupportedOperationException(UNSUPPORTED_ERROR);
    }

    @Override
    public LinkUserIdentificator getDefaultIdentificator() {
        return DEFAULT_IDENTIFICATOR;
    }

    @Override
    public String getName() {
        return "GOOGLE";
    }
}
