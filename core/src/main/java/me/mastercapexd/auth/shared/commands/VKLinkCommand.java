package me.mastercapexd.auth.shared.commands;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.config.message.Messages;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.model.PlayerIdSupplier;
import com.bivashy.auth.api.shared.commands.MessageableCommandActor;
import com.bivashy.auth.api.type.LinkConfirmationType;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.responses.GetResponse;

import me.mastercapexd.auth.hooks.VkPluginHook;
import me.mastercapexd.auth.link.user.confirmation.BaseLinkConfirmationUser;
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

    public VKLinkCommand(LinkConfirmationType linkConfirmationType, Messages<?> messages) {
        super(linkConfirmationType, messages, VKLinkType.getInstance());
    }

    @Default
    @VkUse
    public void vkLink(MessageableCommandActor commandActor, PlayerIdSupplier idSupplier, @Optional LinkUserIdentificator linkUserIdentificator) {
        String accountId = idSupplier.getPlayerId();

        accountStorage.getAccountFromName(accountId).thenAccept(account -> {
            if (isInvalidAccount(account, commandActor, VKLinkType.LINK_USER_FILTER))
                return;
            String code = generateCode(() -> config.getVKSettings().getConfirmationSettings().generateCode());

            long timeoutTimestamp = System.currentTimeMillis() + VKLinkType.getInstance().getSettings().getConfirmationSettings().getRemoveDelay().getMillis();
            sendLinkConfirmation(commandActor, getLinkConfirmationType().bindLinkConfirmationUser(
                    new BaseLinkConfirmationUser(getLinkConfirmationType(), timeoutTimestamp, VKLinkType.getInstance(), account, code), linkUserIdentificator));
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
