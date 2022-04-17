package me.mastercapexd.auth.vk.accounts;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import com.ubivashka.vk.bungee.VKAPI;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.VKEnterAnswer;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.bungee.events.EntryConfirmationSelectEvent;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.config.messages.vk.VKMessageContext;
import me.mastercapexd.auth.link.google.GoogleLinkType;
import me.mastercapexd.auth.link.user.LinkUser;
import me.mastercapexd.auth.link.vk.VKLinkType;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.storage.AccountStorage;
import net.md_5.bungee.api.ProxyServer;

public class VKEntryAccount {
	private final static Random random = new Random();
	private final Account account;
	private final Integer vkId;
	private final String buttonUuid = UUID.randomUUID().toString().substring(0, 16);
	private final long startTime = System.currentTimeMillis();

	public VKEntryAccount(Account account, Integer vkId) {
		this.account = account;
		this.vkId = vkId;
	}

	public Account getAccount() {
		return account;
	}

	public Integer getVkId() {
		return vkId;
	}

	public String getButtonUuid() {
		return buttonUuid;
	}

	public boolean hasCooldownPassed(Integer secondsAdd) {
		return System.currentTimeMillis() > startTime + (secondsAdd * 1000);
	}

	public void enterConnect(VKEnterAnswer answer, PluginConfig config, AccountStorage accountStorage) {
		EntryConfirmationSelectEvent entryConfirmationSelectEvent = new EntryConfirmationSelectEvent(vkId, account,
				answer);
		ProxyServer.getInstance().getPluginManager().callEvent(entryConfirmationSelectEvent);
		if (entryConfirmationSelectEvent.isCancelled())
			return;
		Auth.getLinkEntryAuth().removeLinkUser(account.getId(), VKLinkType.getInstance());

		VKMessageContext messageContext = new VKMessageContext(vkId, account);
		if (answer == VKEnterAnswer.DECLINE) {
			sendMessage(vkId, config.getVKSettings().getMessages().getMessage("enter-kicked", messageContext));
			account.kick(config.getProxyMessages().getSubMessages("vk").getStringMessage("enter-declined"));
		}
		if (answer == VKEnterAnswer.CONFIRM) {
			LinkUser linkUser = account.findFirstLinkUser(GoogleLinkType.LINK_USER_FILTER).orElse(null);
			if (linkUser == null || linkUser.getLinkUserInfo().getIdentificator().asString().isEmpty()
					&& AuthPlugin.getInstance().getConfig().getGoogleAuthenticatorSettings().isEnabled()) {
				//Auth.addGoogleAuthAccount(account);
				return;
			}
			Auth.removeAccount(account.getId());
			sendMessage(vkId, config.getVKSettings().getMessages().getMessage("enter-accepted", messageContext));
			accountStorage.saveOrUpdateAccount(account);

			account.getPlayer().ifPresent(proxyPlayer -> {
				proxyPlayer.sendMessage(
						config.getProxyMessages().getSubMessages("vk").getStringMessage("enter-confirmed"));
				proxyPlayer.sendTo(config.findServerInfo(config.getGameServers()).asProxyServer());
			});
		}
	}

	private boolean sendMessage(Integer userId, String message) {
		try {
			VKAPI.getInstance().getVK().messages().send(VKAPI.getInstance().getActor()).randomId(random.nextInt())
					.userId(userId).message(message).execute();
			return true;
		} catch (ApiException | ClientException e) {
			e.printStackTrace();
			return false;
		}
	}
}
