package me.mastercapexd.auth.telegram.command;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.lamp.telegram.core.TelegramHandler;

import me.mastercapexd.auth.hooks.TelegramPluginHook;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.telegram.TelegramCommandActorWrapper;
import me.mastercapexd.auth.link.telegram.TelegramLinkType;
import me.mastercapexd.auth.messenger.commands.MessengerCommandRegistry;
import me.mastercapexd.auth.shared.commands.MessengerLinkCommandTemplate;
import me.mastercapexd.auth.shared.commands.TelegramLinkCommand;
import revxrsal.commands.CommandHandler;

public class TelegramCommandRegistry extends MessengerCommandRegistry {
    private static final AuthPlugin PLUGIN = AuthPlugin.instance();
    private static final TelegramPluginHook TELEGRAM_HOOK = PLUGIN.getHook(TelegramPluginHook.class);
    private static final CommandHandler COMMAND_HANDLER = new TelegramHandler(TELEGRAM_HOOK.getTelegramBot());

    public TelegramCommandRegistry() {
        super(COMMAND_HANDLER, TelegramLinkType.getInstance());
        COMMAND_HANDLER.registerContextResolver(LinkCommandActorWrapper.class, context -> new TelegramCommandActorWrapper(context.actor()));
        registerCommands();

        startBot();
    }

    private void startBot() {
        TELEGRAM_HOOK.getTelegramBot().setUpdatesListener(new TelegramCommandUpdatesListener(), exception -> {
            exception.printStackTrace();
            if (exception.response() == null)
                return;
            if (exception.response().errorCode() == 409) {// Multiple bot instances error.
                TELEGRAM_HOOK.getTelegramBot().removeGetUpdatesListener();
                System.err.println("Telegram bot disabled because you are already running another bot instance!");
                System.err.println("Please use another token if you need to run multiple bot instances");
            }
        });
    }

    @Override
    protected MessengerLinkCommandTemplate createLinkCommand() {
        return new TelegramLinkCommand(TelegramLinkType.getInstance().getLinkMessages());
    }
}
