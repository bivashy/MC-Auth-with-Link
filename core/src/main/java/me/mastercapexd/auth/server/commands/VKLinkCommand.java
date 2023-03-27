package me.mastercapexd.auth.server.commands;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.link.user.info.impl.UserNumberIdentificator;
import com.bivashy.auth.api.server.player.ServerPlayer;
import com.bivashy.auth.api.type.LinkConfirmationType;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.responses.GetResponse;

import me.mastercapexd.auth.config.message.server.BaseServerMessages;
import me.mastercapexd.auth.hooks.VkPluginHook;
import me.mastercapexd.auth.link.user.confirmation.BaseConfirmationInfo;
import me.mastercapexd.auth.link.vk.VKConfirmationUser;
import me.mastercapexd.auth.link.vk.VKLinkType;
import me.mastercapexd.auth.server.commands.annotations.VkUse;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.orphan.OrphanCommand;

public class VKLinkCommand extends MessengerLinkCommandTemplate implements OrphanCommand {
    private static final String VK_MESSAGES_KEY = "vk";
    @Dependency
    private AuthPlugin plugin;
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountDatabase accountStorage;
    @Dependency
    private BaseServerMessages messages;

    public VKLinkCommand() {
        super(VK_MESSAGES_KEY, VKLinkType.getInstance());
    }

    @Default
    @VkUse
    public void vkLink(ServerPlayer player, @Optional String vkIdentificator) {
        if (vkIdentificator == null) {
            player.sendMessage(messages.getSubMessages(VK_MESSAGES_KEY).getMessage("usage"));
            return;
        }

        String accountId = config.getActiveIdentifierType().getId(player);

        plugin.getCore().runAsync(() -> {
            GetResponse user = fetchUserFromIdentificator(vkIdentificator).orElse(null);
            if (user == null) {
                player.sendMessage(messages.getSubMessages(VK_MESSAGES_KEY).getMessage("user-not-exists"));
                return;
            }

            if (plugin.getLinkEntryBucket().hasLinkUser(accountId, VKLinkType.getInstance())) {
                player.sendMessage(messages.getSubMessages(VK_MESSAGES_KEY).getMessage("already-sent"));
                return;
            }
            int userId = user.getId();

            accountStorage.getAccount(accountId).thenAccept(account -> {
                if (!isValidAccount(account, player, VKLinkType.LINK_USER_FILTER))
                    return;
                LinkUserIdentificator identificator = new UserNumberIdentificator(userId);
                accountStorage.getAccountsFromLinkIdentificator(identificator).thenAccept(accounts -> {
                    if (!isValidLinkAccounts(accounts, player))
                        return;
                    String code = config.getVKSettings().getConfirmationSettings().generateCode();

                    sendLinkConfirmation(identificator, player,
                            new VKConfirmationUser(account, new BaseConfirmationInfo(identificator, code, LinkConfirmationType.FROM_GAME)), accountId);
                });
            });
        });
    }

    private java.util.Optional<GetResponse> fetchUserFromIdentificator(String vkIdentificator) {
        try {
            VkPluginHook vkHook = AuthPlugin.instance().getHook(VkPluginHook.class);
            return vkHook.getClient().users().get(vkHook.getActor()).userIds(vkIdentificator).execute().stream().findFirst();
        } catch(ApiException | ClientException e) {
            e.printStackTrace();
            return java.util.Optional.empty();
        }
    }
}
