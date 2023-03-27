package me.mastercapexd.auth.shared.commands;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.config.message.Messages;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.link.user.info.impl.UserNumberIdentificator;
import com.bivashy.auth.api.model.PlayerIdSupplier;
import com.bivashy.auth.api.shared.commands.MessageableCommandActor;
import com.bivashy.auth.api.type.LinkConfirmationType;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.responses.GetResponse;

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
    @Dependency
    private AuthPlugin plugin;
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountDatabase accountStorage;
    private Messages<?> messages;

    public VKLinkCommand(LinkConfirmationType linkConfirmationType, Messages<?> messages) {
        super(linkConfirmationType, messages, VKLinkType.getInstance());
        this.messages = messages;
    }

    @Default
    @VkUse
    public void vkLink(MessageableCommandActor commandActor, PlayerIdSupplier idSupplier, @Optional String vkIdentificator) {
        if (vkIdentificator == null) {
            commandActor.replyWithMessage(messages.getMessage("usage"));
            return;
        }

        String accountId = idSupplier.getPlayerId();

        plugin.getCore().runAsync(() -> {
            GetResponse user = fetchUserFromIdentificator(vkIdentificator).orElse(null);
            if (user == null) {
                commandActor.replyWithMessage(messages.getMessage("user-not-exists"));
                return;
            }

            if (plugin.getLinkEntryBucket().hasLinkUser(accountId, VKLinkType.getInstance())) {
                commandActor.replyWithMessage(messages.getMessage("already-sent"));
                return;
            }
            int userId = user.getId();

            accountStorage.getAccount(accountId).thenAccept(account -> {
                if (isInvalidAccount(account, commandActor, VKLinkType.LINK_USER_FILTER))
                    return;
                LinkUserIdentificator identificator = new UserNumberIdentificator(userId);
                accountStorage.getAccountsFromLinkIdentificator(identificator).thenAccept(accounts -> {
                    if (isInvalidLinkAccounts(accounts, commandActor))
                        return;
                    String code = config.getVKSettings().getConfirmationSettings().generateCode();

                    sendLinkConfirmation(identificator, commandActor,
                            new VKConfirmationUser(account, new BaseConfirmationInfo(identificator, code, getLinkConfirmationType())), accountId);
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
