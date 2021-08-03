package me.mastercapexd.auth.vk.accounts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.google.gson.Gson;
import com.ubivashka.vk.bungee.VKAPI;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.messages.KeyboardButton;
import com.vk.api.sdk.objects.messages.KeyboardButtonAction;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;
import com.vk.api.sdk.objects.messages.TemplateActionTypeNames;

import me.mastercapexd.auth.Account;
import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.PluginConfig;
import me.mastercapexd.auth.VKEnterAnswer;
import me.mastercapexd.auth.storage.AccountStorage;
import me.mastercapexd.auth.utils.Connector;
import me.mastercapexd.auth.utils.ListUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class VKEntryAccount {
	private final static Gson gson = new Gson();
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
		Auth.removeEntryAccount(account.getId());
		Auth.removeAccount(account.getId());
		ProxiedPlayer proxiedPlayer = account.getIdentifierType().getPlayer(account.getId());
		if (answer == VKEnterAnswer.DECLINE) {
			sendMessage(vkId, config.getVKMessages().getMessage("enter-kicked", account));
			account.kick(config.getBungeeMessages().getLegacyMessage("vk-enter-declined"));
		}
		if (answer == VKEnterAnswer.CONFIRM) {
			sendMessage(vkId, config.getVKMessages().getMessage("enter-confirmed", account));
			accountStorage.saveOrUpdateAccount(account);
			if (proxiedPlayer != null)
				proxiedPlayer.sendMessage(config.getBungeeMessages().getMessage("vk-enter-confirmed"));
			Connector.connectOrKick(proxiedPlayer, config.findServerInfo(config.getGameServers()),
					config.getBungeeMessages().getMessage("game-servers-connection-refused"));
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
