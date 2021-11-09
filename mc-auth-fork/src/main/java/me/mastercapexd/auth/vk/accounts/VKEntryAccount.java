package me.mastercapexd.auth.vk.accounts;

import java.util.Random;
import java.util.UUID;

import com.ubivashka.vk.bungee.VKAPI;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

import me.mastercapexd.auth.Account;
import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.PluginConfig;
import me.mastercapexd.auth.VKEnterAnswer;
import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.bungee.events.EntryConfirmationSelectEvent;
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
		Auth.removeEntryAccount(account.getId());
		ProxiedPlayer proxiedPlayer = account.getIdentifierType().getPlayer(account.getId());
		if (answer == VKEnterAnswer.DECLINE) {
			sendMessage(vkId, config.getVKMessages().getMessage("enter-kicked", vkId, account));
			account.kick(config.getBungeeMessages().getLegacyMessage("vk-enter-declined"));
		}
		if (answer == VKEnterAnswer.CONFIRM) {
			if (account.getGoogleKey() != null && !account.getGoogleKey().isEmpty()
					&& AuthPlugin.getInstance().getConfig().getGoogleAuthenticatorSettings().isEnabled()) {
				Auth.addGoogleAuthAccount(account);
				return;
			}
			Auth.removeAccount(account.getId());
			sendMessage(vkId, config.getVKMessages().getMessage("enter-confirmed", vkId, account));
			accountStorage.saveOrUpdateAccount(account);
			if (proxiedPlayer != null) {
				proxiedPlayer.sendMessage(config.getBungeeMessages().getMessage("vk-enter-confirmed"));
				Connector.connectOrKick(proxiedPlayer, config.findServerInfo(config.getGameServers()),
						config.getBungeeMessages().getMessage("game-servers-connection-refused"));
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
