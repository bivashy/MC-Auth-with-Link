package me.mastercapexd.auth.vk.accounts;

import java.util.ArrayList;
import java.util.List;

import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.messages.KeyboardButton;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;

import me.mastercapexd.auth.Account;
import me.mastercapexd.auth.KickResult;
import me.mastercapexd.auth.PluginConfig;
import me.mastercapexd.auth.RestoreResult;
import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.bungee.events.VKUnlinkEvent;
import me.mastercapexd.auth.storage.AccountStorage;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;
import net.md_5.bungee.api.ProxyServer;

public class VKLinkedAccount {
	private final AuthPlugin plugin;
	private final PluginConfig config;
	private final AccountStorage accountStorage;

	private final Integer userID;

	private final Account account;

	public VKLinkedAccount(VKReceptioner receptioner, Integer userID, Account account) {
		this.config = receptioner.getConfig();
		this.plugin = receptioner.getPlugin();
		this.accountStorage = receptioner.getAccountStorage();
		this.userID = userID;
		this.account = account;
	}

	public void kick() {
		if (!config.getVKSettings().isAdminUser(userID))
			if (account.getVKId().intValue() != userID.intValue()) {
				sendMessage(userID, config.getVKMessages().getMessage("not-your-account", userID, account));
				return;
			}
		sendMessage(userID, config.getVKMessages().getMessage("kick-starting", userID, account));
		KickResult result = account.kick(config.getBungeeMessages().getLegacyMessage("vk-kicked"));
		if (result == KickResult.PLAYER_OFFLINE)
			sendMessage(userID, config.getVKMessages().getMessage("player-offline", userID, account));
		if (result == KickResult.KICKED)
			sendMessage(userID, config.getVKMessages().getMessage("kicked", userID, account));
	}

	public void unlink() {
		if (!config.getVKSettings().isAdminUser(userID))
			if (account.getVKId().intValue() != userID.intValue()) {
				sendMessage(userID, config.getVKMessages().getMessage("not-your-account", userID, account));
				return;
			}
		VKUnlinkEvent event = new VKUnlinkEvent(userID, account);
		ProxyServer.getInstance().getPluginManager().callEvent(event);
		if (event.isCancelled())
			return;
		sendMessage(userID, config.getVKMessages().getMessage("unlinked", userID, account));
		account.setVKId(-1);
		accountStorage.saveOrUpdateAccount(account);
	}

	public void restore() {
		RestoreResult result = account.restoreAccount(userID, config.getVKSettings().isAdminUser(userID),
				config.getVKSettings().getRestoreSettings().getCodeLength());
		if (!config.getVKSettings().isAdminUser(userID))
			if (result == RestoreResult.ACCOUNT_VK_NOT_EQUALS) {
				sendMessage(userID, config.getVKMessages().getMessage("not-your-account", userID, account));
				return;
			}

		account.logout(config.getSessionDurability());
		account.kick(config.getBungeeMessages().getLegacyMessage("vk-kicked"));
		sendMessage(userID, config.getVKMessages().getMessage("restored", userID, account).replaceAll("(?i)%password%",
				result.getPasswordHash()));
		accountStorage.saveOrUpdateAccount(account);
	}

	public void sendAccountSettingsKeyboard() {
		Keyboard keyboard = new Keyboard();
		keyboard.setInline(true);
		List<KeyboardButton> buttons = new ArrayList<>();
		buttons.add(plugin.getVKUtils().buildCallbackButton("restore", account, "restore_" + account.getId(),
				KeyboardButtonColor.PRIMARY));
		buttons.add(plugin.getVKUtils().buildCallbackButton("kick", account, "kick_" + account.getId(),
				KeyboardButtonColor.PRIMARY));
		if (account.getVKId() != -1)
			buttons.add(plugin.getVKUtils().buildCallbackButton("unlink", account, "unlink_" + account.getId(),
					KeyboardButtonColor.PRIMARY));
		if (config.getVKSettings().getEnterSettings().canToggleEnterConfirmation()) {
			if (account.isVKConfirmationEnabled()) {
				buttons.add(plugin.getVKUtils().buildCallbackButton("disable-confirmation",
						"toogle-confirmation_" + account.getId(), KeyboardButtonColor.NEGATIVE));
			} else {
				buttons.add(plugin.getVKUtils().buildCallbackButton("enable-confirmation",
						"toogle-confirmation_" + account.getId(), KeyboardButtonColor.POSITIVE));
			}
		}
		buttons.add(plugin.getVKUtils().buildCallbackButton("return", account, "return", KeyboardButtonColor.DEFAULT));
		keyboard.setButtons(plugin.getListUtils().chopList(buttons, 3));
		plugin.getVKUtils().sendMessage(userID, config.getVKMessages().getMessage("account-control", userID, account),
				keyboard);
	}

	private void sendMessage(Integer userID, String message) {
		plugin.getVKUtils().sendMessage(userID, message);
	}

}
