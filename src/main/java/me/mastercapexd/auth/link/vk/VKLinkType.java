package me.mastercapexd.auth.link.vk;

import com.google.common.base.Predicate;
import com.ubivashka.messenger.vk.MessengerVk;

import me.mastercapexd.auth.account.factories.AccountFactory;
import me.mastercapexd.auth.config.message.Messages;
import me.mastercapexd.auth.config.messenger.MessengerSettings;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.link.user.LinkUser;
import me.mastercapexd.auth.link.user.info.identificator.LinkUserIdentificator;
import me.mastercapexd.auth.link.user.info.identificator.UserNumberIdentificator;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.message.ProxyComponent;

public class VKLinkType implements LinkType, MessengerVk {
	public static final Predicate<LinkUser> LINK_USER_FILTER = (linkUser) -> linkUser.getLinkType() == getInstance();
	private static final VKLinkType INSTANCE = new VKLinkType();
	private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();
	private static final LinkUserIdentificator DEFAULT_IDENTIFICATOR = new UserNumberIdentificator(
			AccountFactory.DEFAULT_VK_ID);

	private VKLinkType() {
	}

	public static VKLinkType getInstance() {
		return INSTANCE;
	}

	@Override
	public Messages<ProxyComponent> getProxyMessages() {
		return PLUGIN.getConfig().getProxyMessages().getSubMessages("vk");
	}

	@Override
	public Messages<String> getLinkMessages() {
		return PLUGIN.getConfig().getVKSettings().getMessages();
	}

	@Override
	public MessengerSettings getSettings() {
		return PLUGIN.getConfig().getVKSettings();
	}

	@Override
	public LinkUserIdentificator getDefaultIdentificator() {
		return DEFAULT_IDENTIFICATOR;
	}

}
