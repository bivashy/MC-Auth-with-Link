package me.mastercapexd.auth.messenger.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.util.CollectionUtil.ArrayPairHashMapAdapter.PaginatedList;
import com.bivashy.messenger.common.button.ButtonColor;
import com.bivashy.messenger.common.keyboard.Keyboard;

import me.mastercapexd.auth.discord.command.annotation.RenameTo;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.messenger.commands.annotation.CommandKey;
import me.mastercapexd.auth.shared.commands.annotation.CommandCooldown;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Flag;
import revxrsal.commands.orphan.OrphanCommand;

@CommandKey(AccountsListCommand.CONFIGURATION_KEY)
public class AccountsListCommand implements OrphanCommand {

    public static final String CONFIGURATION_KEY = "accounts";
    @Dependency
    private AccountDatabase accountDatabase;
    @Dependency
    private LinkType linkType;

    @DefaultFor("~")
    @CommandCooldown(CommandCooldown.DEFAULT_VALUE)
    public void onAccountsMenu(LinkCommandActorWrapper actorWrapper, LinkType linkType, @Flag("page") @Default("1") Integer page,
                               @RenameTo(value = "size", type = "NUMBER") @Flag("pageSize") @Default("5") Integer accountsPerPage,
                               @Flag("type") @Default("my") AccountListType type) {
        if (!linkType.getSettings().isAdministrator(actorWrapper.userId()) && type.isAdministratorOnly) {
            actorWrapper.reply(linkType.getLinkMessages().getMessage("not-enough-permission"));
            return;
        }

        CompletableFuture<Collection<Account>> accountsCollection = type.getAccounts(accountDatabase, linkType, actorWrapper);

        accountsCollection.thenAccept(accounts -> {
            if (accounts.isEmpty()) {
                actorWrapper.reply(linkType.getLinkMessages().getMessage(type.accountsNotFound));
                return;
            }

            List<Account> paginatedAccounts = new PaginatedList<>(accountsPerPage, accounts).getPage(page);
            if (paginatedAccounts.isEmpty()) {
                actorWrapper.reply(linkType.getLinkMessages().getMessage("no-page-accounts"));
                return;
            }
            Keyboard keyboard = createKeyboard(linkType, page, accountsPerPage, type.name(), paginatedAccounts);
            actorWrapper.send(linkType.newMessageBuilder(linkType.getLinkMessages().getMessage(type.accountsMessage)).keyboard(keyboard).build());
        });
    }

    private Keyboard createKeyboard(LinkType linkType, int currentPage, int accountsPerPage, String accountsType, List<Account> accounts) {
        int previousPage = currentPage - 1;
        int nextPage = currentPage + 1;
        List<String> placeholdersList = new ArrayList<>(
                Arrays.asList("%next_page%", Integer.toString(nextPage), "%previous_page%", Integer.toString(previousPage), "%prev_page%",
                        Integer.toString(currentPage - 1), "%pageSize%", Integer.toString(accountsPerPage), "%type%", accountsType));

        for (int i = 1; i <= accounts.size(); i++) { // Create placeholders array
            Account account = accounts.get(i - 1);
            placeholdersList.add("%account_" + i + "%");
            placeholdersList.add(account.getName());

            placeholdersList.add("%account_" + i + "_color%");
            ButtonColor buttonColor = account.getPlayer().isPresent() ?
                    linkType.newButtonColorBuilder().green() : linkType.newButtonColorBuilder().red();
            placeholdersList.add(buttonColor.asJsonValue());
        }
        // We are replacing all remaining button colors with any color for preventing 'parsing' error
        placeholdersList.add("%account_._color%");
        placeholdersList.add(linkType.newButtonColorBuilder().white().asJsonValue());
        Keyboard keyboard = linkType.getSettings().getKeyboards().createKeyboard("accounts", placeholdersList.toArray(new String[0]));

        // Remove buttons that doesn't affected by placeholders (For example if player has linked accounts count is less than accounts.size())
        keyboard.removeIf(button -> button.getActionData().contains("%account"));
        return keyboard;
    }

    public enum AccountListType {
        ALL(true, "admin-panel-no-accounts", "admin-panel-accounts") {
            @Override
            CompletableFuture<Collection<Account>> getAccounts(AccountDatabase database, LinkType linkType, LinkCommandActorWrapper actorWrapper) {
                return database.getAllAccounts();
            }
        }, LINKED(true, "admin-panel-no-linked-accounts", "admin-panel-linked-accounts") {
            @Override
            CompletableFuture<Collection<Account>> getAccounts(AccountDatabase database, LinkType linkType, LinkCommandActorWrapper actorWrapper) {
                return database.getAllLinkedAccounts();
            }
        }, MY(false, "no-accounts", "accounts") {
            @Override
            CompletableFuture<Collection<Account>> getAccounts(AccountDatabase database, LinkType linkType, LinkCommandActorWrapper actorWrapper) {
                return database.getAccountsFromLinkIdentificator(actorWrapper.userId());
            }
        }, LOCAL_LINKED(true, "admin-panel-no-linked-accounts", "admin-panel-linked-accounts") {
            @Override
            CompletableFuture<Collection<Account>> getAccounts(AccountDatabase database, LinkType linkType, LinkCommandActorWrapper actorWrapper) {
                return database.getAllLinkedAccounts(linkType);
            }
        };
        private final boolean isAdministratorOnly;
        private final String accountsNotFound, accountsMessage;

        AccountListType(boolean isAdministratorOnly, String accountsNotFound, String accountsMessage) {
            this.isAdministratorOnly = isAdministratorOnly;
            this.accountsNotFound = accountsNotFound;
            this.accountsMessage = accountsMessage;
        }

        abstract CompletableFuture<Collection<Account>> getAccounts(AccountDatabase database, LinkType linkType, LinkCommandActorWrapper actorWrapper);
    }

}
