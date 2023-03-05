package me.mastercapexd.auth.server.commands;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.config.message.MessageContext;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.link.user.LinkUser;
import com.bivashy.auth.api.link.user.info.impl.UserNumberIdentificator;
import com.bivashy.auth.api.server.player.ServerPlayer;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.responses.GetResponse;

import me.mastercapexd.auth.config.message.server.BaseServerMessages;
import me.mastercapexd.auth.hooks.VkPluginHook;
import me.mastercapexd.auth.link.user.confirmation.BaseConfirmationInfo;
import me.mastercapexd.auth.link.vk.VKConfirmationUser;
import me.mastercapexd.auth.link.vk.VKLinkType;
import me.mastercapexd.auth.server.commands.annotations.VkUse;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Optional;

@Command({"addvk", "vkadd", "vklink", "linkvk"})
public class VKLinkCommand {
    private static final String VK_SUBMESSAGES_KEY = "vk";
    @Dependency
    private AuthPlugin plugin;
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountDatabase accountStorage;
    @Dependency
    private BaseServerMessages messages;

    @Default
    @VkUse
    public void vkLink(ServerPlayer player, @Optional String vkIdentificator) {
        if (vkIdentificator == null) {
            player.sendMessage(messages.getSubMessages(VK_SUBMESSAGES_KEY).getMessage("usage"));
            return;
        }

        String accountId = config.getActiveIdentifierType().getId(player);

        plugin.getCore().runAsync(() -> {
            GetResponse user = fetchUserFromIdentificator(vkIdentificator).orElse(null);
            if (user == null) {
                player.sendMessage(messages.getSubMessages(VK_SUBMESSAGES_KEY).getMessage("user-not-exists"));
                return;
            }

            if (plugin.getLinkEntryBucket().hasLinkUser(accountId, VKLinkType.getInstance())) {
                player.sendMessage(messages.getSubMessages(VK_SUBMESSAGES_KEY).getMessage("already-sent"));
                return;
            }
            int userId = user.getId();

            accountStorage.getAccount(accountId).thenAccept(account -> {
                if (account == null || !account.isRegistered()) {
                    player.sendMessage(config.getServerMessages().getMessage("account-not-found"));
                    return;
                }
                LinkUser linkUser = account.findFirstLinkUserOrNew(VKLinkType.LINK_USER_FILTER, VKLinkType.getInstance());
                if (!linkUser.isIdentifierDefaultOrNull()) {
                    player.sendMessage(messages.getSubMessages(VK_SUBMESSAGES_KEY).getMessage("already-linked"));
                    return;
                }
                UserNumberIdentificator userIdentificator = new UserNumberIdentificator(userId);
                accountStorage.getAccountsFromLinkIdentificator(userIdentificator).thenAccept(accounts -> {
                    if (config.getVKSettings().getMaxLinkCount() != 0 && accounts.size() >= config.getVKSettings().getMaxLinkCount()) {
                        player.sendMessage(messages.getSubMessages(VK_SUBMESSAGES_KEY).getMessage("link-limit-reached"));
                        return;
                    }
                    String code = config.getVKSettings().getConfirmationSettings().generateCode();

                    plugin.getLinkConfirmationBucket()
                            .removeLinkUsers(linkConfirmationUser -> linkConfirmationUser.getAccount().getPlayerId().equals(accountId) &&
                                    linkConfirmationUser.getLinkUserInfo().getIdentificator().equals(userIdentificator));
                    plugin.getLinkConfirmationBucket().addLinkUser(new VKConfirmationUser(account, new BaseConfirmationInfo(userIdentificator, code)));
                    player.sendMessage(messages.getSubMessages(VK_SUBMESSAGES_KEY).getMessage("confirmation-sent", MessageContext.of("%code%", code)));
                });
            });
        });
    }

    @VkUse
    @Command({"link vk", "vk link", "add vk", "vk add"})
    public void link(ServerPlayer player, @Optional String vkIdentificator) {
        vkLink(player, vkIdentificator);
    }

    private java.util.Optional<GetResponse> fetchUserFromIdentificator(String vkIdentificator) {
        try {
            VkPluginHook vkHook = AuthPlugin.instance().getHook(VkPluginHook.class);
            return vkHook.getClient().users().get(vkHook.getActor()).userIds(vkIdentificator).execute().stream().findFirst();
        } catch(ApiException | ClientException e) {
            e.printStackTrace();
        }
        return java.util.Optional.empty();
    }
}
