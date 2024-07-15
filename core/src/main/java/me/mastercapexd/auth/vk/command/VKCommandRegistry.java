package me.mastercapexd.auth.vk.command;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.lamp.commands.vk.core.VkHandler;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

import me.mastercapexd.auth.hooks.VkPluginHook;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.vk.VKCommandActorWrapper;
import me.mastercapexd.auth.link.vk.VKLinkType;
import me.mastercapexd.auth.messenger.commands.MessengerCommandRegistry;
import me.mastercapexd.auth.shared.commands.MessengerLinkCommandTemplate;
import me.mastercapexd.auth.shared.commands.VKLinkCommand;
import me.mastercapexd.auth.vk.command.exception.VKExceptionHandler;
import revxrsal.commands.CommandHandler;

public class VKCommandRegistry extends MessengerCommandRegistry {
    private static final VkPluginHook VK_HOOK = AuthPlugin.instance().getHook(VkPluginHook.class);
    private static final CommandHandler COMMAND_HANDLER = new VkHandler(VK_HOOK.getClient(), VK_HOOK.getActor()).disableStackTraceSanitizing();

    public VKCommandRegistry() {
        super(COMMAND_HANDLER, VKLinkType.getInstance());
        COMMAND_HANDLER.registerContextResolver(LinkCommandActorWrapper.class, context -> new VKCommandActorWrapper(context.actor()));
        COMMAND_HANDLER.setExceptionHandler(new VKExceptionHandler(VKLinkType.getInstance()));
        registerCommands();

        try {
            VK_HOOK.getClient().groups().setSettings(VK_HOOK.getActor(), VK_HOOK.getActor().getGroupId()).botsCapabilities(true).messages(true).execute();
            VK_HOOK.getClient()
                    .groups()
                    .setLongPollSettings(VK_HOOK.getActor(), VK_HOOK.getActor().getGroupId())
                    .enabled(true)
                    .messageEvent(true)
                    .messageNew(true)
                    .apiVersion("5.131")
                    .execute();
        } catch(ApiException | ClientException e) {
            e.printStackTrace();
            System.err.println("Give all permissions to the vk api token for the automatically settings apply.");
        }
    }

    @Override
    protected MessengerLinkCommandTemplate createLinkCommand() {
        return new VKLinkCommand(VKLinkType.getInstance().getLinkMessages());
    }
}
