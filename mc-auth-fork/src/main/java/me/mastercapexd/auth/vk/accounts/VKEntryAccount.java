package me.mastercapexd.auth.vk.accounts;

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
import me.mastercapexd.auth.link.vk.VKLinkType;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.storage.AccountStorage;
import me.mastercapexd.auth.utils.Connector;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
		ProxyPlayer proxyPlayer = account.getIdentifierType().getPlayer(account.getId());

		VKMessageContext messageContext = VKMessageContext.newContext(vkId, account);
		if (answer == VKEnterAnswer.DECLINE) {
			sendMessage(vkId, config.getVKSettings().getVKMessages().getMessage("enter-kicked", messageContext));
			account.kick(config.getProxyMessages().getStringMessage("vk-enter-declined"));
		}
		if (answer == VKEnterAnswer.CONFIRM) {
			if (account.getGoogleKey() != null && !account.getGoogleKey().isEmpty()
					&& AuthPlugin.getInstance().getConfig().getGoogleAuthenticatorSettings().isEnabled()) {
				Auth.addGoogleAuthAccount(account);
				return;
			}
			Auth.removeAccount(account.getId());
			sendMessage(vkId, config.getVKSettings().getVKMessages().getMessage("enter-confirmed", messageContext));
			accountStorage.saveOrUpdateAccount(account);
			if (proxyPlayer != null) {
				proxyPlayer.sendMessage(config.getProxyMessages().getStringMessage("vk-enter-confirmed"));
				proxyPlayer.sendTo(config.findServerInfo(config.getGameServers()).asProxyServer());
			}
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
