package me.mastercapexd.auth.proxy.commands;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.responses.GetResponse;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.config.message.context.MessageContext;
import me.mastercapexd.auth.config.message.proxy.ProxyMessages;
import me.mastercapexd.auth.hooks.VkPluginHook;
import me.mastercapexd.auth.link.user.LinkUser;
import me.mastercapexd.auth.link.user.confirmation.info.DefaultConfirmationInfo;
import me.mastercapexd.auth.link.user.info.identificator.UserNumberIdentificator;
import me.mastercapexd.auth.link.vk.VKConfirmationUser;
import me.mastercapexd.auth.link.vk.VKLinkType;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.commands.annotations.VkUse;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.storage.AccountStorage;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Optional;

@Command({"addvk", "vkadd", "vklink", "linkvk"})
public class VKLinkCommand {
    private static final String VK_SUBMESSAGES_KEY = "vk";
    @Dependency
    private ProxyPlugin plugin;
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountStorage accountStorage;
    @Dependency
    private ProxyMessages messages;

    @Default
    @VkUse
    public void vkLink(ProxyPlayer player, @Optional String vkIdentificator) {
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

            if (Auth.getLinkEntryAuth().hasLinkUser(accountId, VKLinkType.getInstance())) {
                player.sendMessage(messages.getSubMessages(VK_SUBMESSAGES_KEY).getMessage("already-sent"));
                return;
            }
            int userId = user.getId();

            accountStorage.getAccount(accountId).thenAccept(account -> {
                if (account == null || !account.isRegistered()) {
                    player.sendMessage(config.getProxyMessages().getMessage("account-not-found"));
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

                    Auth.getLinkConfirmationAuth()
                            .removeLinkUsers(linkConfirmationUser -> linkConfirmationUser.getAccount().getPlayerId().equals(accountId) &&
                                    linkConfirmationUser.getLinkUserInfo().getIdentificator().equals(userIdentificator));
                    Auth.getLinkConfirmationAuth().addLinkUser(new VKConfirmationUser(account, new DefaultConfirmationInfo(userIdentificator, code)));
                    player.sendMessage(messages.getSubMessages(VK_SUBMESSAGES_KEY).getMessage("confirmation-sent", MessageContext.of("%code%", code)));
                });
            });
        });
    }

    @VkUse
    @Command({"link vk", "vk link", "add vk", "vk add"})
    public void link(ProxyPlayer player, @Optional String vkIdentificator) {
        vkLink(player, vkIdentificator);
    }

    private java.util.Optional<GetResponse> fetchUserFromIdentificator(String vkIdentificator) {
        try {
            VkPluginHook vkHook = ProxyPlugin.instance().getHook(VkPluginHook.class);
            return vkHook.getClient().users().get(vkHook.getActor()).userIds(vkIdentificator).execute().stream().findFirst();
        } catch(ApiException | ClientException e) {
            e.printStackTrace();
        }
        return java.util.Optional.empty();
    }
}
