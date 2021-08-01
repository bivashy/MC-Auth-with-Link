package me.mastercapexd.auth.vk.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.ubivashka.vk.bungee.VKAPI;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Conversation;
import com.vk.api.sdk.objects.messages.ConversationPeerType;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.messages.KeyboardButton;
import com.vk.api.sdk.objects.messages.KeyboardButtonAction;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;
import com.vk.api.sdk.objects.messages.TemplateActionTypeNames;

import me.mastercapexd.auth.Account;
import me.mastercapexd.auth.PluginConfig;
import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.bungee.BungeeAccount;
import me.mastercapexd.auth.storage.AccountStorage;
import me.mastercapexd.auth.vk.VKAccountsPageType;
import me.mastercapexd.auth.vk.accounts.VKEntryAccount;
import net.md_5.bungee.api.ProxyServer;

public class VKUtils {
	private static final VkApiClient vk = VKAPI.getInstance().getVK();
	private static final GroupActor actor = VKAPI.getInstance().getActor();
	private static final Random random = new Random();
	private static final Gson gson = new Gson();

	private AuthPlugin plugin;
	private PluginConfig config;
	private AccountStorage accountStorage;

	public VKUtils(AuthPlugin plugin, PluginConfig config, AccountStorage accountStorage) {
		this.plugin = plugin;
		this.config = config;
		this.accountStorage = accountStorage;
	}

	public KeyboardButton createSettingsButtonFromAccount(Account account) {
		return buildCallbackButton("account", account, "account_" + account.getId(),
				(ProxyServer.getInstance().getPlayer(account.getUniqueId()) == null) ? KeyboardButtonColor.NEGATIVE
						: KeyboardButtonColor.POSITIVE);
	}

	public List<KeyboardButton> createPageButtons(int size, int page, VKAccountsPageType type) {
		List<KeyboardButton> buttons = new ArrayList<>();
		if (page > 1)
			buttons.add(buildCallbackButton("previous-page", "previouspage_" + page + "_" + type.toString(),
					KeyboardButtonColor.DEFAULT));

		if (page < plugin.getListUtils().getMaxPages(size, 5))
			buttons.add(buildCallbackButton("next-page", "nextpage_" + page + "_" + type.toString(),
					KeyboardButtonColor.DEFAULT));

		return buttons;
	}

	public void sendAccountsKeyboard(Integer userId, Integer page, Collection<Account> accounts,
			VKAccountsPageType type) {
		Consumer<Collection<Account>> accountsConsumer = getAccountsConsumer(userId, page, type);
		accountsConsumer.accept(accounts);
	}

	public void sendAccountsKeyboard(Integer userId, Integer page) {
		Consumer<Collection<Account>> accountsConsumer = getAccountsConsumer(userId, page, VKAccountsPageType.OWNPAGE);
		accountStorage.getAccountsByVKID(userId).thenAccept(accounts -> {
			accountsConsumer.accept(accounts);
		});
	}

	public void sendAdminPanel(Integer userId) {
		if (!config.getVKSettings().isAdminUser(userId))
			return;
		Keyboard keyboard = new Keyboard();
		keyboard.setInline(true);
		List<KeyboardButton> buttons = new ArrayList<>();
		buttons.add(buildCallbackButton("admin-panel-all-accounts", "allAccounts", KeyboardButtonColor.PRIMARY));
		buttons.add(buildCallbackButton("admin-panel-all-linked-accounts", "allLinkedAccounts",
				KeyboardButtonColor.PRIMARY));
		keyboard.setButtons(plugin.getListUtils().chopList(buttons, 3));
		sendMessage(userId, config.getVKMessages().getLegacyMessage("admin-panel"), keyboard);
	}

	public void sendConfirmationMessage(VKEntryAccount entryAccount) {
		Keyboard enterKeyboard = new Keyboard();
		enterKeyboard.setInline(true);
		List<KeyboardButton> buttons = new ArrayList<>();
		buttons.add(buildCallbackButton("enter-confirm", entryAccount.getAccount(),
				"enterserver_confirm_" + entryAccount.getButtonUuid(), KeyboardButtonColor.POSITIVE));
		buttons.add(buildCallbackButton("enter-decline", entryAccount.getAccount(),
				"enterserver_decline_" + entryAccount.getButtonUuid(), KeyboardButtonColor.NEGATIVE));
		enterKeyboard.setButtons(plugin.getListUtils().chopList(buttons, 2));
		sendMessage(entryAccount.getVkId(),
				config.getVKMessages().getMessage("enter-message", entryAccount.getAccount()), enterKeyboard);
	}

	public boolean isChat(Integer peerId) {
		if (peerId == null)
			return false;
		try {
			List<Conversation> conversations = vk.messages().getConversationsById(actor, peerId).execute().getItems();
			if (conversations.isEmpty())
				return false;
			return conversations.get(0).getPeer().getType() == ConversationPeerType.CHAT;
		} catch (ApiException | ClientException e) {
			return false;
		}
	}

	public boolean sendMessage(Integer userId, String message) {
		try {
			vk.messages().send(actor).randomId(random.nextInt()).userId(userId).message(message).execute();
			return true;
		} catch (ApiException | ClientException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean sendMessage(Integer userId, String message, Keyboard keyboard) {
		try {
			vk.messages().send(actor).randomId(random.nextInt()).userId(userId).message(message).keyboard(keyboard)
					.execute();
			return true;
		} catch (ApiException | ClientException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Integer getVKIdFromScreenName(String screenName) {
		try {
			return vk.utils().resolveScreenName(actor, screenName).execute().getObjectId();
		} catch (Exception e) {
			return -1;
		}

	}

	private KeyboardButton buildCallbackButton(String labelPath, String payload, KeyboardButtonColor color) {
		return new KeyboardButton()
				.setAction(new KeyboardButtonAction().setLabel(config.getVKButtonLabels().getButtonLabel(labelPath))
						.setType(TemplateActionTypeNames.CALLBACK).setPayload(gson.toJson(payload)))
				.setColor(color);
	}

	public KeyboardButton buildCallbackButton(String labelPath, Account account, String payload,
			KeyboardButtonColor color) {
		return new KeyboardButton().setAction(
				new KeyboardButtonAction().setLabel(config.getVKButtonLabels().getButtonLabel(labelPath, account))
						.setType(TemplateActionTypeNames.CALLBACK).setPayload(gson.toJson(payload)))
				.setColor(color);
	}

	private Consumer<Collection<Account>> getAccountsConsumer(Integer userId, Integer page, VKAccountsPageType type) {
		return (findedAccounts -> {
			if (findedAccounts.isEmpty()) {
				sendMessage(userId, config.getVKMessages().getLegacyMessage("no-accounts"));
				return;
			}
			List<BungeeAccount> sortedAccounts = findedAccounts.stream().map(account -> (BungeeAccount) account)
					.sorted().collect(Collectors.toList());
			List<BungeeAccount> listAccounts = plugin.getListUtils().getListPage(sortedAccounts, page, 5);
			Keyboard keyboard = new Keyboard();
			keyboard.setInline(true);
			List<KeyboardButton> accountButtons = new ArrayList<>();
			for (Account account : listAccounts)
				accountButtons.add(createSettingsButtonFromAccount(account));
			keyboard.setButtons(plugin.getListUtils().chopList(accountButtons, 1));
			if (sortedAccounts.size() > 5) {
				List<KeyboardButton> pageButtons = createPageButtons(sortedAccounts.size(), page, type);
				keyboard.getButtons().add(pageButtons);
			}
			sendMessage(userId, config.getVKMessages().getLegacyMessage("accounts"), keyboard);
		});
	}

}
