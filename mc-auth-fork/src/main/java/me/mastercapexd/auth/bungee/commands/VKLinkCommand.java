package me.mastercapexd.auth.bungee.commands;

import com.vk.api.sdk.objects.users.responses.GetResponse;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.account.factories.AccountFactory;
import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.bungee.commands.annotations.VkUse;
import me.mastercapexd.auth.bungee.commands.exception.BungeeSendableException;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.config.messages.Messages;
import me.mastercapexd.auth.link.confirmation.info.DefaultConfirmationInfo;
import me.mastercapexd.auth.link.confirmation.vk.VKConfirmationUser;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;
import me.mastercapexd.auth.link.user.info.identificator.UserNumberIdentificator;
import me.mastercapexd.auth.link.vk.VKLinkType;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.storage.AccountStorage;
import me.mastercapexd.auth.utils.RandomCodeFactory;
import me.mastercapexd.auth.vk.utils.VKUtils;
import net.md_5.bungee.api.ProxyServer;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Optional;

@Command({ "addvk", "vkadd", "vklink", "linkvk" })
public class VKLinkCommand {
	private static final Messages<ProxyComponent> VK_MESSAGES = ProxyPlugin.instance().getConfig().getProxyMessages().getSubMessages("vk");
	@Dependency
	private AuthPlugin plugin;
	@Dependency
	private PluginConfig config;
	@Dependency
	private AccountStorage accountStorage;

	@Default
	@VkUse
	public void vkLink(ProxyPlayer player, @Optional String vkIdentificator) {
		if (vkIdentificator == null)
			throw new BungeeSendableException(VK_MESSAGES.getStringMessage("usage"));

		String accountId = config.getActiveIdentifierType().getId(player);

		ProxyServer.getInstance().getScheduler().runAsync(plugin, () -> {
			GetResponse user = VKUtils.fetchUserFromIdentificator(vkIdentificator).orElse(null);
			if (user == null) {
				player.sendMessage(VK_MESSAGES.getStringMessage("user-not-exists"));
				return;
			}

			if (Auth.getLinkEntryAuth().hasLinkUser(accountId, VKLinkType.getInstance())) {
				player.sendMessage(VK_MESSAGES.getStringMessage("already-sent"));
				return;
			}
			Integer userId = user.getId();

			accountStorage.getAccount(accountId).thenAccept(account -> {
				if (account == null || !account.isRegistered()) {
					player.sendMessage(config.getProxyMessages().getStringMessage("account-not-found"));
					return;
				}
				LinkUserInfo vkLinkInfo = account.findFirstLinkUser(VKLinkType.LINK_USER_FILTER).orElse(null)
						.getLinkUserInfo();

				if (vkLinkInfo.getIdentificator() != null
						&& vkLinkInfo.getIdentificator().asNumber() != AccountFactory.DEFAULT_VK_ID) {
					player.sendMessage(VK_MESSAGES.getStringMessage("already-linked"));
					return;
				}
				accountStorage.getAccountsByVKID(userId).thenAccept(accounts -> {
					if (config.getVKSettings().getMaxLinkCount() != 0
							&& accounts.size() >= config.getVKSettings().getMaxLinkCount()) {
						player.sendMessage(VK_MESSAGES.getStringMessage("link-limit-reached"));
						return;
					}
					String code = RandomCodeFactory
							.generateCode(config.getVKSettings().getConfirmationSettings().getCodeLength());

					Auth.getLinkConfirmationAuth().addLinkUser(new VKConfirmationUser(account,
							new DefaultConfirmationInfo(new UserNumberIdentificator(userId), code)));
					player.sendMessage(VK_MESSAGES.getStringMessage("confirmation-sent")
							.replaceAll("(?i)%code%", code));
				});
			});

		});
	}

	@VkUse
	@Command({ "link vk", "vk link", "add vk", "vk add" })
	public void link(ProxyPlayer player, @Optional String vkIdentificator) {
		vkLink(player, vkIdentificator);
	}

}
