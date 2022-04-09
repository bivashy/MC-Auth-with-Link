package me.mastercapexd.auth.vk.accounts;

import java.util.ArrayList;
import java.util.List;

import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.messages.KeyboardButton;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;

import me.mastercapexd.auth.KickResult;
import me.mastercapexd.auth.RestoreResult;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.account.factories.AccountFactory;
import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.bungee.events.VKUnlinkEvent;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.config.messages.vk.VKMessageContext;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;
import me.mastercapexd.auth.link.vk.VKLinkType;
import me.mastercapexd.auth.storage.AccountStorage;
import me.mastercapexd.auth.utils.CollectionUtils;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;
import net.md_5.bungee.api.ProxyServer;

public class VKLinkedAccount {
	private final AuthPlugin plugin;
	private final PluginConfig config;
	private final AccountStorage accountStorage;

	private final Integer userID;

	private final Account account;

	private final VKMessageContext messageContext;

	public VKLinkedAccount(VKReceptioner receptioner, Integer userID, Account account) {
		this.config = receptioner.getConfig();
		this.plugin = receptioner.getPlugin();
		this.accountStorage = receptioner.getAccountStorage();
		this.userID = userID;
		this.account = account;

		this.messageContext = VKMessageContext.newContext(userID, account);
	}

	public void kick() {

		if (!config.getVKSettings().isAdminUser(userID))
			if (account.findFirstLinkUser(VKLinkType.getLinkUserPredicate()).orElse(null).getLinkUserInfo()
					.getIdentificator().asNumber() != userID) {
				sendMessage(userID,
						config.getVKSettings().getVKMessages().getMessage("not-your-account", messageContext));
				return;
			}
		sendMessage(userID, config.getVKSettings().getVKMessages().getMessage("kick-starting", messageContext));
		KickResult result = account.kick(config.getProxyMessages().getStringMessage("vk-kicked"));
		if (result == KickResult.PLAYER_OFFLINE)
			sendMessage(userID, config.getVKSettings().getVKMessages().getMessage("player-offline", messageContext));
		if (result == KickResult.KICKED)
			sendMessage(userID, config.getVKSettings().getVKMessages().getMessage("kicked", messageContext));
	}

	public void unlink() {
		if (!config.getVKSettings().isAdminUser(userID))
			if (account.findFirstLinkUser(VKLinkType.getLinkUserPredicate()).orElse(null).getLinkUserInfo()
					.getIdentificator().asNumber() != userID) {
				sendMessage(userID,
						config.getVKSettings().getVKMessages().getMessage("not-your-account", messageContext));
				return;
			}
		VKUnlinkEvent event = new VKUnlinkEvent(userID, account);
		ProxyServer.getInstance().getPluginManager().callEvent(event);
		if (event.isCancelled())
			return;
		sendMessage(userID, config.getVKSettings().getVKMessages().getMessage("unlinked", messageContext));
		account.findFirstLinkUser(VKLinkType.getLinkUserPredicate()).orElse(null).getLinkUserInfo()
				.getIdentificator().setNumber(AccountFactory.DEFAULT_VK_ID);
		accountStorage.saveOrUpdateAccount(account);
	}

	public void restore() {
//		RestoreResult result = account.restoreAccount(userID, config.getVKSettings().isAdminUser(userID),
//				config.getVKSettings().getRestoreSettings().getCodeLength());
//		if (!config.getVKSettings().isAdminUser(userID))
//			if (result == RestoreResult.ACCOUNT_VK_NOT_EQUALS) {
//				sendMessage(userID,
//						config.getVKSettings().getVKMessages().getMessage("not-your-account", messageContext));
//				return;
//			}
//
//		account.logout(config.getSessionDurability());
//		account.kick(config.getBungeeMessages().getStringMessage("vk-kicked"));
//		sendMessage(userID, config.getVKSettings().getVKMessages().getMessage("restored", messageContext)
//				.replaceAll("(?i)%password%", result.getPasswordHash()));
//		accountStorage.saveOrUpdateAccount(account);
	}

	public void sendAccountSettingsKeyboard() {
		Keyboard keyboard = new Keyboard();
		keyboard.setInline(true);
		List<KeyboardButton> buttons = new ArrayList<>();
		buttons.add(plugin.getVKUtils().buildCallbackButton("restore", account, "restore_" + account.getId(),
				KeyboardButtonColor.PRIMARY));
		buttons.add(plugin.getVKUtils().buildCallbackButton("kick", account, "kick_" + account.getId(),
				KeyboardButtonColor.PRIMARY));

		LinkUserInfo vkLinkInfo = account.findFirstLinkUser(VKLinkType.getLinkUserPredicate()).orElse(null)
				.getLinkUserInfo();

		if (vkLinkInfo.getIdentificator().asNumber() != AccountFactory.DEFAULT_VK_ID)
			buttons.add(plugin.getVKUtils().buildCallbackButton("unlink", account, "unlink_" + account.getId(),
					KeyboardButtonColor.PRIMARY));
		if (config.getVKSettings().getEnterSettings().canToggleEnterConfirmation()) {
			if (account.findFirstLinkUser(VKLinkType.getLinkUserPredicate()).orElse(null).getLinkUserInfo()
					.getConfirmationState().shouldSendConfirmation()) {
				buttons.add(plugin.getVKUtils().buildCallbackButton("disable-confirmation",
						"toogle-confirmation_" + account.getId(), KeyboardButtonColor.NEGATIVE));
			} else {
				buttons.add(plugin.getVKUtils().buildCallbackButton("enable-confirmation",
						"toogle-confirmation_" + account.getId(), KeyboardButtonColor.POSITIVE));
			}
		}
		buttons.add(plugin.getVKUtils().buildCallbackButton("return", account, "return", KeyboardButtonColor.DEFAULT));
		keyboard.setButtons(CollectionUtils.chopList(buttons, 3));
		plugin.getVKUtils().sendMessage(userID,
				config.getVKSettings().getVKMessages().getMessage("account-control", messageContext), keyboard);
	}

	private void sendMessage(Integer userID, String message) {
		plugin.getVKUtils().sendMessage(userID, message);
	}

}
