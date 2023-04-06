package me.mastercapexd.auth.messenger.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.util.CollectionUtil.ArrayPairHashMapAdapter.PaginatedList;
import com.ubivaska.messenger.common.button.ButtonColor;
import com.ubivaska.messenger.common.keyboard.Keyboard;

import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Flag;
import revxrsal.commands.orphan.OrphanCommand;

public class AccountsListCommand implements OrphanCommand {
    public static final String CONFIGURATION_KEY = "accounts";
    @Dependency
    private AccountDatabase accountDatabase;

    @Default
    public void onAccountsMenu(LinkCommandActorWrapper actorWrapper, LinkType linkType, @Flag("page") @Default("1") Integer page, @Flag("pageSize") @Default(
            "5") Integer accountsPerPage, @Flag("type") @Default("my") String type) {
        if (!linkType.getSettings().isAdministrator(actorWrapper.userId()) && (type.equalsIgnoreCase("all") || type.equalsIgnoreCase("linked"))) {
            actorWrapper.reply(linkType.getLinkMessages().getMessage("not-enough-permission"));
            return;
        }

        CompletableFuture<Collection<Account>> accountsCollection = CompletableFuture.completedFuture(Collections.emptyList());

        switch (type.toLowerCase()) {
            case "all":
                accountsCollection = accountDatabase.getAllAccounts();
                break;
            case "linked":
                accountsCollection = accountDatabase.getAllLinkedAccounts();
                break;
            case "my":
                accountsCollection = accountDatabase.getAccountsFromLinkIdentificator(actorWrapper.userId());
                break;
        }

        accountsCollection.thenAccept(accounts -> {
            if (accounts.isEmpty()) {
                actorWrapper.reply(linkType.getLinkMessages().getMessage("no-accounts"));
                return;
            }

            List<Account> paginatedAccounts = new PaginatedList<>(accountsPerPage, accounts).getPage(page);
            if (paginatedAccounts.isEmpty()) {
                actorWrapper.reply(linkType.getLinkMessages().getMessage("no-page-accounts"));
                return;
            }
            Keyboard keyboard = createKeyboard(linkType, page, accountsPerPage, type, paginatedAccounts);
            actorWrapper.send(linkType.newMessageBuilder(linkType.getLinkMessages().getMessage("accounts")).keyboard(keyboard).build());
        });
    }

    private Keyboard createKeyboard(LinkType linkType, int currentPage, int accountsPerPage, String accountsType, List<Account> accounts) {
        List<String> placeholdersList = new ArrayList<>(Arrays.asList("%next_page%", Integer.toString(currentPage + 1), "%previous_page%",
                Integer.toString(currentPage - 1), "%prev_page%", Integer.toString(currentPage - 1), "%pageSize%", Integer.toString(accountsPerPage), "%type" +
                        "%", accountsType));

        for (int i = 1; i <= accounts.size(); i++) { // Create placeholders array
            Account account = accounts.get(i - 1);
            placeholdersList.add("%account_" + i + "%");
            placeholdersList.add(account.getName());

            placeholdersList.add("%account_" + i + "_color%");
            ButtonColor buttonColor = account.getPlayer().isPresent() ? linkType.newButtonColorBuilder().green() : linkType.newButtonColorBuilder().red();
            placeholdersList.add(buttonColor.toString());
        }
        Keyboard keyboard = linkType.getSettings().getKeyboards().createKeyboard("accounts", placeholdersList.toArray(new String[0]));

        keyboard.removeIf(button -> button.getActionData().contains("%account"));
        return keyboard;
    }
}
