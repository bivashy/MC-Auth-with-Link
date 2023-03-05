package me.mastercapexd.auth.link.vk;

import java.util.function.Predicate;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.account.AccountFactory;
import com.bivashy.auth.api.config.link.LinkSettings;
import com.bivashy.auth.api.config.message.Messages;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.LinkUser;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.link.user.info.impl.UserNumberIdentificator;
import com.bivashy.auth.api.server.message.ServerComponent;
import com.ubivashka.messenger.vk.MessengerVk;

import me.mastercapexd.auth.config.message.link.context.LinkPlaceholderContext;
import me.mastercapexd.auth.config.message.vk.VKMessagePlaceholderContext;

public class VKLinkType implements LinkType, MessengerVk {
    private static final VKLinkType INSTANCE = new VKLinkType();
    public static final Predicate<LinkUser> LINK_USER_FILTER = (linkUser) -> linkUser.getLinkType() == getInstance();
    private static final AuthPlugin PLUGIN = AuthPlugin.instance();
    private static final LinkUserIdentificator DEFAULT_IDENTIFICATOR = new UserNumberIdentificator(AccountFactory.DEFAULT_VK_ID);

    private VKLinkType() {
    }

    public static VKLinkType getInstance() {
        return INSTANCE;
    }

    @Override
    public Messages<ServerComponent> getServerMessages() {
        return PLUGIN.getConfig().getServerMessages().getSubMessages("vk");
    }

    @Override
    public Messages<String> getLinkMessages() {
        return PLUGIN.getConfig().getVKSettings().getMessages();
    }

    @Override
    public LinkSettings getSettings() {
        return PLUGIN.getConfig().getVKSettings();
    }

    @Override
    public LinkUserIdentificator getDefaultIdentificator() {
        return DEFAULT_IDENTIFICATOR;
    }

    @Override
    public LinkPlaceholderContext newMessageContext(Account account) {
        return new VKMessagePlaceholderContext(account);
    }
}
