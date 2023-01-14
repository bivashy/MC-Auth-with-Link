package me.mastercapexd.auth.proxy.commands;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.responses.GetResponse;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.account.factories.AccountFactory;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.config.message.Messages;
import me.mastercapexd.auth.hooks.VkPluginHook;
import me.mastercapexd.auth.link.user.confirmation.info.DefaultConfirmationInfo;
import me.mastercapexd.auth.link.user.info.LinkUserInfo;
import me.mastercapexd.auth.link.user.info.identificator.UserNumberIdentificator;
import me.mastercapexd.auth.link.vk.VKConfirmationUser;
import me.mastercapexd.auth.link.vk.VKLinkType;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.commands.annotations.VkUse;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.storage.AccountStorage;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Optional;

@Command({"addvk", "vkadd", "vklink", "linkvk"})
public class VKLinkCommand {
    private static final Messages<ProxyComponent> VK_MESSAGES = ProxyPlugin.instance().getConfig().getProxyMessages().getSubMessages("vk");
    @Dependency
    private ProxyPlugin plugin;
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountStorage accountStorage;

    @Default
    @VkUse
    public void vkLink(ProxyPlayer player, @Optional String vkIdentificator) {
        if (vkIdentificator == null) {
            player.sendMessage(VK_MESSAGES.getStringMessage("usage"));
            return;
        }

        String accountId = config.getActiveIdentifierType().getId(player);

        plugin.getCore().runAsync(() -> {
            GetResponse user = fetchUserFromIdentificator(vkIdentificator).orElse(null);
            if (user == null) {
                player.sendMessage(VK_MESSAGES.getStringMessage("user-not-exists"));
                return;
            }

            if (Auth.getLinkEntryAuth().hasLinkUser(accountId, VKLinkType.getInstance())) {
                player.sendMessage(VK_MESSAGES.getStringMessage("already-sent"));
                return;
            }
            Integer userId = user.getId();

            accountStorage.getAccount(accountId).thenAccept(account -> {
                if (account == null || !account.isRegistered()) {
                    player.sendMessage(config.getProxyMessages().getStringMessage("account-not-found"));
                    return;
                }
                LinkUserInfo vkLinkInfo = account.findFirstLinkUserOrNew(VKLinkType.LINK_USER_FILTER, VKLinkType.getInstance()).getLinkUserInfo();

                if (vkLinkInfo.getIdentificator() != null && vkLinkInfo.getIdentificator().asNumber() != AccountFactory.DEFAULT_VK_ID) {
                    player.sendMessage(VK_MESSAGES.getStringMessage("already-linked"));
                    return;
                }
                accountStorage.getAccountsByVKID(userId).thenAccept(accounts -> {
                    if (config.getVKSettings().getMaxLinkCount() != 0 && accounts.size() >= config.getVKSettings().getMaxLinkCount()) {
                        player.sendMessage(VK_MESSAGES.getStringMessage("link-limit-reached"));
                        return;
                    }
                    String code = config.getVKSettings().getConfirmationSettings().generateCode();
                    UserNumberIdentificator userIdentificator = new UserNumberIdentificator(userId);

                    Auth.getLinkConfirmationAuth().removeLinkUsers(linkConfirmationUser -> linkConfirmationUser.getAccount().getPlayerId().equals(accountId) &&
                            linkConfirmationUser.getLinkUserInfo().getIdentificator().equals(userIdentificator));
                    Auth.getLinkConfirmationAuth().addLinkUser(new VKConfirmationUser(account, new DefaultConfirmationInfo(userIdentificator,
                            code)));
                    player.sendMessage(VK_MESSAGES.getStringMessage("confirmation-sent").replaceAll("(?i)%code%", code));
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
