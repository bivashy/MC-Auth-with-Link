package me.mastercapexd.auth.messenger.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.link.message.keyboard.IKeyboard;
import me.mastercapexd.auth.link.message.keyboard.button.ButtonColor;
import me.mastercapexd.auth.storage.AccountStorage;
import me.mastercapexd.auth.utils.CollectionUtils;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Flag;
import revxrsal.commands.orphan.OrphanCommand;

public class AccountsListCommand implements OrphanCommand {
	@Dependency
	private AccountStorage accountStorage;

	/**
	 * Command that sends accounts keyboard to the user.
	 * 
	 * @param actorWrapper    Actor that executed command
	 * @param linkType        Messenger where command executed
	 * @param page            Page that need to show
	 * @param accountsPerPage One page limit
	 * @param type
	 * 
	 *                        <pre>
	 *             Accounts type, available types:
	 *             all - Show all accounts. Only admins can use this accounts type.
	 *             linked - Show all linked accounts . Only admins can use this accounts type.
	 *             my - Show all accounts that linked to the actorWrapper
	 *                        </pre>
	 */
	@Default
	public void onAccountsMenu(LinkCommandActorWrapper actorWrapper, LinkType linkType,
			@Flag("page") @Default("1") Integer page, @Flag("pageSize") @Default("5") Integer accountsPerPage,
			@Flag("type") @Default("my") String type) {
		if (!linkType.getSettings().isAdministrator(actorWrapper.userId()) && (type.equalsIgnoreCase("all")
				|| type.equalsIgnoreCase("linked"))) {
			actorWrapper.reply(linkType.getLinkMessages().getMessage("not-enough-permission"));
			return;
		}

		CompletableFuture<Collection<Account>> accountsCollection = CompletableFuture
				.completedFuture(Collections.emptyList());

		switch (type.toLowerCase()) {
		case "all":
			accountsCollection = accountStorage.getAllAccounts();
			break;
		case "linked":
			accountsCollection = accountStorage.getAllLinkedAccounts();
			break;
		case "my":
			accountsCollection = accountStorage.getAccountsFromLinkIdentificator(actorWrapper.userId());
			break;
		}

		accountsCollection.thenAccept(accounts -> {
			if (accounts.isEmpty()) {
				actorWrapper.reply(linkType.getLinkMessages().getMessage("no-accounts"));
				return;
			}
			List<Account> paginatedAccounts = CollectionUtils.getListPage(new ArrayList<>(accounts), page,
					accountsPerPage);

			IKeyboard keyboard = createKeyboard(linkType, page, accountsPerPage, type, paginatedAccounts);
			actorWrapper.send(linkType.newMessageBuilder().rawContent(linkType.getLinkMessages().getMessage("accounts"))
					.keyboard(keyboard).build());
		});
	}

	private IKeyboard createKeyboard(LinkType linkType, int currentPage, int accountsPerPage, String accountsType,
			List<Account> accounts) {
		List<String> placeholdersList = new ArrayList<>(Arrays.asList("%next_page%", Integer.toString(currentPage + 1),
				"%previous_page%", Integer.toString(currentPage-1), "%pageSize%", Integer.toString(accountsPerPage),
				"%type%", accountsType));

		for (int i = 1; i <= accounts.size(); i++) { // Create placeholders array
			Account account = accounts.get(i - 1);
			placeholdersList.add("%account_" + i + "%");
			placeholdersList.add(account.getName());

			placeholdersList.add("%account_" + i + "_color%");
			ButtonColor buttonColor = account.getPlayer().isPresent() ? linkType.newButtonColorBuilder().green()
					: linkType.newButtonColorBuilder().red();
			placeholdersList.add(buttonColor.toText());
		}
		IKeyboard keyboard = linkType.getSettings().getKeyboards().createKeyboard("accounts",
				placeholdersList.toArray(new String[0]));

		keyboard.removeIf(button -> button.getLabel().contains("%account"));
		return keyboard;
	}
}
