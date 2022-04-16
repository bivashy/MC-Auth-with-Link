package me.mastercapexd.auth.vk.builders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.messages.KeyboardButton;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.utils.CollectionUtils;
import me.mastercapexd.auth.vk.VKAccountsPageType;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;
import me.mastercapexd.auth.vk.utils.VKUtils;

public class AccountsMessageBuilder extends MessageBuilder {
	private final Integer userId, page;
	private final Collection<Account> accounts;
	private final VKAccountsPageType type;
	private final VKUtils vkUtils;
	private final PluginConfig config;

	public AccountsMessageBuilder(Integer userId, Integer page, VKAccountsPageType type, Collection<Account> accounts,
			VKReceptioner receptioner) {
		this.userId = userId;
		this.page = page;
		this.type = type;
		this.accounts = accounts;
		this.vkUtils = receptioner.getPlugin().getVKUtils();
		this.config = receptioner.getConfig();
	}

	@Override
	public MessagesSendQuery build() {
		return getAccountsConsumer().apply(accounts);
	}

	private Function<Collection<Account>, MessagesSendQuery> getAccountsConsumer() {
		return (findedAccounts -> {
			MessagesSendQuery sendQuery = CLIENT.messages().send(ACTOR).randomId(RANDOM.nextInt()).userId(userId);
			if (findedAccounts.isEmpty()) {
				sendQuery.message(config.getVKSettings().getVKMessages().getMessage(type.getNoAccountsPath()));
				return sendQuery;
			}
			List<Account> listAccounts = CollectionUtils.getListPage(sortAccounts(accounts), page, 5);
			Keyboard keyboard = createKeyboard(listAccounts, findedAccounts);
			sendQuery.keyboard(keyboard)
					.message(config.getVKSettings().getVKMessages().getMessage(type.getAccountsPath()));
			return sendQuery;
		});
	}

	private Keyboard createKeyboard(List<Account> accounts, Collection<Account> allAccounts) {
		Keyboard keyboard = new Keyboard().setInline(true);

		List<KeyboardButton> accountButtons = new ArrayList<>();
		accounts.forEach(account -> accountButtons.add(createSettingsButtonFromAccount(account)));
		keyboard.setButtons(CollectionUtils.chopList(accountButtons, 1));

		List<KeyboardButton> pageButtons = createPageButtons(allAccounts.size(), page, type);
		if (!pageButtons.isEmpty())
			keyboard.getButtons().add(pageButtons);

		return keyboard;
	}

	private List<Account> sortAccounts(Collection<Account> accounts) {
		List<Account> sortedAccounts = new ArrayList<>(accounts);
		Collections.sort(sortedAccounts, new Comparator<Account>() {

			@Override
			public int compare(Account o1, Account o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		return sortedAccounts;
	}

	private KeyboardButton createSettingsButtonFromAccount(Account account) {
		return vkUtils.buildCallbackButton("account", account, "account_" + account.getId(),
				account.getPlayer().isPresent() ? KeyboardButtonColor.NEGATIVE
						: KeyboardButtonColor.POSITIVE);
	}

	private List<KeyboardButton> createPageButtons(int size, int page, VKAccountsPageType type) {
		List<KeyboardButton> buttons = new ArrayList<>();
		if (page > 1)
			buttons.add(vkUtils.buildCallbackButton("previous-page", "previouspage_" + page + "_" + type.toString(),
					KeyboardButtonColor.DEFAULT));

		if (page < CollectionUtils.getMaxPages(size, 5))
			buttons.add(vkUtils.buildCallbackButton("next-page", "nextpage_" + page + "_" + type.toString(),
					KeyboardButtonColor.DEFAULT));

		return buttons;
	}
}
